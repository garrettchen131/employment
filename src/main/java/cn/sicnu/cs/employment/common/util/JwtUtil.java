package cn.sicnu.cs.employment.common.util;

import cn.sicnu.cs.employment.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final Key key; // 用于签名 Access Token
    private final Key refreshKey; // 用于签名 Refresh Token
    private final AppProperties appProperties;

    // 将 appProperties进行解码
    public JwtUtil(AppProperties appProperties) {
        this.appProperties = appProperties;     // 获取appProperties中的Key，app中的Key从配置文件中获取
        key = new SecretKeySpec(Base64.getDecoder().decode(appProperties.getJwt().getKey()), "HmacSHA512");
        refreshKey = new SecretKeySpec(Base64.getDecoder().decode(appProperties.getJwt().getRefreshKey()), "HmacSHA512");
    }

    /**
     * 生成一个Token
     * @param userDetails payLoad负载信息
     * @param timeToExpire Token的有效期
     */
    public String createJWTToken(UserDetails userDetails, long timeToExpire) {
        return createJWTToken(userDetails, timeToExpire, key);
    }

    /**
     * 根据用户信息生成一个 JWT
     *
     * @param userDetails  用户信息
     * @param timeToExpire 毫秒单位的失效时间
     * @param signKey      签名使用的 key
     * @return JWT
     */
    public String createJWTToken(UserDetails userDetails, long timeToExpire, Key signKey) {
        return Jwts
            .builder()
            .setId("sicnu")     // id：登录用户id？
            .setSubject(userDetails.getUsername())      // subject：表示签名所面向的用户，即用户名
            .claim("authorities",             // claim :设置自保存信息
                userDetails.getAuthorities().stream()       // 录入"authorities"权限
                    .map(GrantedAuthority::getAuthority)        // 从 JWTFilter的 this::setSpringAuthentication 后中获取用户权限
                    .collect(Collectors.toList()))              // 将其转成 List
            .setIssuedAt(new Date(System.currentTimeMillis()))      // 表示在什么时候签发的Token
            .setExpiration(new Date(System.currentTimeMillis() + timeToExpire))     // 设置过期时间
            .signWith(signKey, SignatureAlgorithm.HS512).compact();         // 签名
    }

    public String createAccessToken(UserDetails userDetails) {
        return createJWTToken(userDetails, appProperties.getJwt().getAccessTokenExpireTime());
    }

    public String createRefreshToken(UserDetails userDetails) {
        return createJWTToken(userDetails, appProperties.getJwt().getRefreshTokenExpireTime(), refreshKey);
    }

    public boolean validateAccessToken(String jwtToken) {
        return validateToken(jwtToken, key);
    }

    public boolean validateRefreshToken(String jwtToken) {
        return validateToken(jwtToken, refreshKey);
    }

    /**
     * 返回Token是否有效
     */
    public boolean validateToken(String jwtToken, Key signKey) {
        return parseClaims(jwtToken, signKey).isPresent();
    }

    public String buildAccessTokenWithRefreshToken(String jwtToken) {
        return parseClaims(jwtToken, refreshKey)
            .map(claims -> Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + appProperties.getJwt().getAccessTokenExpireTime()))
                .signWith(key, SignatureAlgorithm.HS512).compact())
            .orElseThrow();
    }

    /**
     * 解析验证Token
     * @param jwtToken 接收的Token
     * @param signKey  签名
     * @return 保存的信息（claims）
     */
    public Optional<Claims> parseClaims(String jwtToken, Key signKey) {
        return Optional.ofNullable(Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(jwtToken).getBody());
    }

    /**
     * 不检查是否过期（验证Token是否有效）
     */
    public boolean validateWithoutExpiration(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
            return true;
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            if (e instanceof ExpiredJwtException) {
                return true;
            }
        }
        return false;
    }

    public Key getKey() {
        return key;
    }

    public Key getRefreshKey() {
        return refreshKey;
    }

    /**
     * 设置Token的Cookie用于返回给前端
     */
    public void setCookiesForTokens(HttpServletResponse res, UserDetails user) {
        val accessToken = createAccessToken(user);
        val refreshToken = createRefreshToken(user);
        Cookie accessCookie = new Cookie("access_token", accessToken);
//        accessCookie.setHttpOnly(true);
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
//        refreshCookie.setHttpOnly(true);
        res.addCookie(accessCookie);
        res.addCookie(refreshCookie);
    }
}
