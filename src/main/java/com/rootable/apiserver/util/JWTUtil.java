package com.rootable.apiserver.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Map;

@Log4j2
@Component
public class JWTUtil {

    private static Key key;

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    /*
    * 토큰 생성
    * */
    public static String generateToken(Map<String, Object> valueMap, int min) {

        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();

    }

    /*
    * 토큰 검증
    * */
    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claims = null;

        try {
            //토큰 복호화
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) //파싱 및 검증, 실패 시 에러
                    .getBody();
        } catch (MalformedJwtException malformedJwtException) { //올바르지 않은 토큰값
            throw new CustomJWTException("MalFormed");
        } catch (ExpiredJwtException expiredJwtException) { //토큰 만료
            throw new CustomJWTException("Expired");
        } catch (InvalidClaimException invalidClaimException) { //올바르지 않은 Claim 필드값
            throw new CustomJWTException("Invalid");
        } catch (JwtException jwtException) { //그 외 JWT 관련 예외
            throw new CustomJWTException("JWTError");
        } catch (Exception e) { //그 외 예외
            throw new CustomJWTException("Error");
        }

        return claims;

    }

}
