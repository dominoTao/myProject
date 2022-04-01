package com.example.myproject;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

public class JWTTest {

    // token 存在时长
    private static final long RANGE = 1000 * 60 * 60 * 24;

    // 签名
    private static final String SIGNATURE = "admin";

    @Test
    public void jwt(){
        JwtBuilder jwtBuilder = Jwts.builder();
        final String jwtToken = jwtBuilder
                // header
                .setHeaderParam("typ", "jwt")
                .setHeaderParam("alg", "SH256")
                // payload
                .claim("username", "tom")
                .claim("role", "admin")
                .setExpiration(new Date(System.currentTimeMillis() + RANGE))  // 超时时间
                .setSubject("jwt-test")     //主题
                .setId(UUID.randomUUID().toString())        // id
                // signature
                .signWith(SignatureAlgorithm.HS256, SIGNATURE)      // 签名算法
                .compact();     // 对上面三部分进行拼接
        System.out.println(jwtToken);
//eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRvbSIsInJvbGUiOiJhZG1pbiIsImV4cCI6MTY0ODkwNzA4MCwic3ViIjoiand0LXRlc3QiLCJqdGkiOiI5NWUxYzYyYi1jZDA1LTRlNTEtOTk5YS01NGZkODdhYmYxNzMifQ.ZMvLoSQ2REijU11e-7ZvARZ6Plp34PHAKRlBwIm-AMo
    }

    @Test
    public void parse(){
        final String jwtToken = "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRvbSIsInJvbGUiOiJhZG1pbiIsImV4cCI6MTY0ODkwNzA4MCwic3ViIjoiand0LXRlc3QiLCJqdGkiOiI5NWUxYzYyYi1jZDA1LTRlNTEtOTk5YS01NGZkODdhYmYxNzMifQ.ZMvLoSQ2REijU11e-7ZvARZ6Plp34PHAKRlBwIm-AMo";
        JwtParser jwtParser = Jwts.parser();
        final Jws<Claims> claimsJws = jwtParser.setSigningKey(SIGNATURE).parseClaimsJws(jwtToken);
        final Claims body = claimsJws.getBody();
        System.out.println(body.get("username"));
        System.out.println(body.get("role"));
        System.out.println(body.getId());
        System.out.println(body.getExpiration().getTime());
        System.out.println(body.getSubject());
    }
}
