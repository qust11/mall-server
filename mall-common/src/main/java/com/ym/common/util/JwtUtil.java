package com.ym.common.util;

import com.ym.common.dto.UserCommonDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static String secretStr = "q9G6AWi2cYQ3K4IzDUGjFzbFEW4fxjR+l2r/5SJ/6pA=";

    // 加密密钥
    private static SecretKey secretKey;

    private static final String USER_ID_CLAIM = "userId";
    private static final String PHONE_CLAIM = "phone";

    // token 有效期 30天小时
    private static final long EXPIRE_MILLS = 30 * 24 * 60 * 60 * 1000l;

    @PostConstruct
    public void initSecretKey() {
        byte[] decodeBytes = Base64.getDecoder().decode(secretStr);
        JwtUtil.secretKey = Keys.hmacShaKeyFor(decodeBytes);
    }

    /**
     * 生成JWT token 0.13新API
     */
    public static String generateToken(Long userId, String phone) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expireAt = new Date(now + EXPIRE_MILLS);

        return Jwts.builder()
                .issuedAt(issuedAt)
                .expiration(expireAt)
                .claim(USER_ID_CLAIM, userId)
                .claim(PHONE_CLAIM, phone)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 生成JWT token 0.13新API
     */
    public static String generateTokenByUserDto(UserCommonDto userCommonDto) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expireAt = new Date(now + EXPIRE_MILLS);

        return Jwts.builder()
                .issuedAt(issuedAt)
                .expiration(expireAt)
                .claim(USER_ID_CLAIM, userCommonDto.getId())
                .claim(PHONE_CLAIM, userCommonDto.getPhone())
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析token获取用户ID
     */
    public static Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaimsByKey(token, USER_ID_CLAIM));
    }

    /**
     * 解析token获取用户名
     */
    public static String getPhoneByToken(String token) {
        return getClaimsByKey(token, PHONE_CLAIM);
    }

    /**
     * 从token中获取信息
     */
    public static String getClaimsByKey(String token, String claimKey) {
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
        Claims payload = jws.getPayload();
        return payload.get(claimKey).toString();
    }

    /**
     * 校验token 的用户名是否与参数一致，及token合法性
     */
    public static Boolean validateToken(String token, String phone) {
        String tokenPhone = getPhoneByToken(token);
        return tokenPhone.equals(phone);
     }

    /**
     * 校验token合法性、是否过期
     */
    public static void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("token已过期，请重新登录");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("token格式错误");
        } catch (SignatureException e) {
            throw new RuntimeException("token签名非法，篡改风险");
        } catch (Exception e) {
            throw new RuntimeException("无效登录凭证");
        }
    }

    /**
     * 获取token过期毫秒数（用于redis登录缓存过期对齐）
     */
    public long getTokenExpireMills() {
        return EXPIRE_MILLS;
    }
    public static void main(String[] args) {
        // 生成标准 HS256 密钥（256bit，满足jjwt0.13强制要求）
        SecretKey secretKey = Jwts.SIG.HS256.key().build();
        // 0.13 用原生Base64编码
        String base64Secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("复制下面字符串到yml jwt.secret：");
        System.out.println(base64Secret);
    }
}