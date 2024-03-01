package com.rootable.apiserver.controller;

import com.rootable.apiserver.util.CustomJWTException;
import com.rootable.apiserver.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {

    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader,
                                       @RequestParam(value = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        String accessToken = authHeader.substring(7);

        /*
        * AccessToken 이 만료되지 않았다면
        * ==> 그대로 사용
        * */
        if (!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        //RefreshToken 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);

        log.info("refresh ... claims: " + claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10);

        String newRefreshToken = checkTime((Integer) claims.get("exp")) ? JWTUtil.generateToken(claims, 60 * 24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);

    }

    /*
    * RefreshToken 이 1시간 미만 남았을 경우
    * ==> RefreshToken 재발급
    * JWT 만료 시간(exp) - seconds
    * */
    private boolean checkTime(Integer exp) {

        //seconds -> milliseconds 로 변환
        Date expDate = new Date((long) exp * 1000);

        //남은 시간 계산
        long remain = expDate.getTime() - System.currentTimeMillis();

        //남은 min 계산 (milliseconds -> minutes)
        long leftMin = remain / (1000 * 60);

        return leftMin < 60;

    }

    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);
        } catch (CustomJWTException ex) {
            if (ex.getMessage().equals("Expired")) {
                return true; //만료
            }
        }

        return false; //만료x
    }

}
