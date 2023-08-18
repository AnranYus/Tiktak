package com.anryus.userservice.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class Jwt {

    final
    StringRedisTemplate template;

    private final RSAPublicKey rsaPublicKey;
    private final RSAPrivateKey rsaPrivateKey;
    public Jwt(StringRedisTemplate template) {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGen.initialize(2048); // 设置密钥长度
        KeyPair keyPair = keyGen.generateKeyPair();
        this.rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        this.rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.template = template;
    }

    public String createToken(long uid,String rule){
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            Map<String,String> map = new HashMap<>();
            map.put("uid",String.valueOf(uid));
            map.put("rule",rule);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withPayload(map)
                    .sign(algorithm);
            saveInRedis(uid,token);
            return token;
        } catch (JWTCreationException exception){
            exception.printStackTrace();
        }
        return null;
    }

    private void saveInRedis(long uid,String token){
        byte[] publicKeyBytes = rsaPublicKey.getEncoded();
        byte[] privateKeyBytes = rsaPrivateKey.getEncoded();
        template.opsForValue().set(String.valueOf(uid),token);
        template.opsForValue().set(token, Base64.getEncoder().encodeToString(publicKeyBytes)+":"+Base64.getEncoder().encodeToString(privateKeyBytes));
    }

    public String getToken(long uid){
        return template.opsForValue().get(String.valueOf(uid));
    }
}
