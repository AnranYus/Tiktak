package com.anryus.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Resource
    StringRedisTemplate template;


    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    public Map<String,String> verify ( String token) {
        Map<String,String> map = new HashMap<>();

        DecodedJWT decodedJWT;
        String s = template.opsForValue().get(token);
        if (s!=null){
            String[] split = s.split(":");

            try {
                RSAPublicKey publicKey = StringToKey(split[0]);
                try {
                    Algorithm algorithm = Algorithm.RSA256(publicKey, null);
                    JWTVerifier verifier = JWT.require(algorithm)
                            .withIssuer("auth0")
                            .build();

                    decodedJWT = verifier.verify(token);
                    Map<String, Claim> claims = decodedJWT.getClaims();

                    String uidStr = claims.get("uid").asString();
                    String rule = claims.get("rule").asString();
                    map.put("uid",uidStr);
                    map.put("rule",rule);

                } catch (JWTVerificationException exception) {
                    exception.printStackTrace();
                }

            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }


        }


        return map;
    }

    private static RSAPublicKey StringToKey(String pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
        byte[] publicKeyBytes = Base64.getDecoder().decode(pubKey); // 将Base64编码的字符串解码为字节数组

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes); // 创建包含公钥字节数组的密钥规范

        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // 获取RSA密钥工厂实例

        return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
    }
}
