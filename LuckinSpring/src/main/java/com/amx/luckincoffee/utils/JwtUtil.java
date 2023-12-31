package com.amx.luckincoffee.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

    // 设置token的私钥
    private static final String TOKEN_SECRET="HI!Yi Zhi AMX~";

    // 设置过期时间为15天
    private static final long EXPIRE_TIME = 15 * 60 * 1000;

    // 生成令牌Token
    public static String createToken(String userName,String telephone){
        try {
            //过期时间：当前时间开始后的15天
            Date date =new Date(System.currentTimeMillis()+EXPIRE_TIME);
            // 不加头部信息，默认为 "alg": "HS256","typ": "JWT"
            return JWT.create()
                    .withClaim("userName",userName)
                    .withClaim("userTelephone",telephone)
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));  // 签名算法采用HMAC256
        }catch (Exception e){
            return null;
        }
    }

    // 验证Token是否合法
    public static boolean verifyToken(String token){
        try{
            // 签名用什么加密算法，验证的时候同样
            Algorithm algorithm=Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier=JWT.require(algorithm).build();
            DecodedJWT JWT=verifier.verify(token);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    // 获取userName
    public static String getUserName(String token){
        try {
            DecodedJWT jwt=JWT.decode(token);
            return  jwt.getClaim("userName").asString();
        }catch (JWTDecodeException ex){
            return null;
        }
    }


    // 获取userTelephone
    public static String getUserTelephone(String token){
        try {
            DecodedJWT jwt=JWT.decode(token);
            return  jwt.getClaim("userTelephone").asString();
        }catch (JWTDecodeException ex){
            return null;
        }
    }
}