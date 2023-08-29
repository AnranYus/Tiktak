package com.anryus.common.utils;

import com.alibaba.nacos.common.utils.StringUtils;
import com.anryus.common.config.CommonConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    final StringRedisTemplate template;

    final CommonConfig config;


    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public JwtUtils(CommonConfig config, StringRedisTemplate template) {
        this.config = config;
        this.template = template;
    }

    public Map<String,String> verify ( String token) {
        Map<String,String> map = new HashMap<>();
        RSAPublicKey publicKey;
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(config.getJksKeyPath()),config.getJksPassword().toCharArray());
            publicKey = (RSAPublicKey) keyStore.getCertificate(config.getJksAlias()).getPublicKey();
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        DecodedJWT decodedJWT;

        try {
            try {
                Algorithm algorithm = Algorithm.RSA256(publicKey, null);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("auth0")
                        .build();
                decodedJWT = verifier.verify(token);
                Map<String, Claim> claims = decodedJWT.getClaims();
                String uidStr = claims.get("uid").asString();
                String s = template.opsForValue().get(uidStr);

                if (StringUtils.isNotEmpty(s) && StringUtils.equals(s, token)){
                    String rule = claims.get("rule").asString();
                    map.put("uid",uidStr);
                    map.put("rule",rule);
                }else {
                    throw new JWTVerificationException("未找到token");
                }


            } catch (JWTVerificationException exception) {
                map.put("reject","true");
                exception.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

}
