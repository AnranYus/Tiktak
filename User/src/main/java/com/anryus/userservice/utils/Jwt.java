package com.anryus.userservice.utils;


import com.anryus.common.config.CommonConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class Jwt {

    final
    StringRedisTemplate template;
    final CommonConfig commonConfig;
    public Jwt(StringRedisTemplate template, CommonConfig commonConfig) {
        this.template = template;
        this.commonConfig = commonConfig;
    }

    public String createToken(long uid,String rule){
        try {
            char[] password = commonConfig.getJksPassword().toCharArray();
            String keyPath = commonConfig.getJksKeyPath();

            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(keyPath),password);

            RSAPrivateKey  privateKey  = (RSAPrivateKey) keyStore.getKey(commonConfig.getJksAlias(),password);
            RSAPublicKey publicKey = (RSAPublicKey) keyStore.getCertificate(commonConfig.getJksAlias()).getPublicKey();

            Algorithm algorithm = Algorithm.RSA256(publicKey,privateKey);
            Map<String,String> map = new HashMap<>();
            map.put("uid",String.valueOf(uid));
            map.put("rule",rule);
            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_MONTH,7);


            String token = JWT.create()
                    .withIssuer("auth0")
                    .withPayload(map)
                    .withExpiresAt(cal.getTime())//7天后过期
                    .sign(algorithm);
            saveInRedis(uid,token);
            return token;
        } catch (JWTCreationException exception){
            exception.printStackTrace();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException |
                 UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void saveInRedis(long uid,String token){
        template.opsForValue().set(String.valueOf(uid),token,7,TimeUnit.DAYS);
    }

    public String getToken(long uid){
        return template.opsForValue().get(String.valueOf(uid));
    }
}
