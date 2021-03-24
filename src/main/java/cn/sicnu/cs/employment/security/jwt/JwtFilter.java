package cn.sicnu.cs.employment.security.jwt;

import cn.sicnu.cs.employment.common.util.CollectionUtil;
import cn.sicnu.cs.employment.common.util.JwtUtil;
import cn.sicnu.cs.employment.config.AppProperties;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AppProperties appProperties;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var claimsOpt = Optional.ofNullable((Claims) null);
        // if【存在符合要求的Token】
        if (checkJwtToken(request)) {
            // 解析获取claims数据，无则null
            claimsOpt = validateToken(request);
            claimsOpt.filter(claims -> Objects.nonNull(claims.get("authorities")))
                    .ifPresentOrElse(
                            this::setSpringAuthentication, // 将claim进行处理，将其中的authorities信息进行保存
                            SecurityContextHolder::clearContext
                    );
        }
        if (claimsOpt.isPresent()) { // 如果claim存在（即完成上面一个if中的代码）
            val un = claimsOpt.get().getSubject();  // 获取用户名
            val userOpt = userMapper.findOptionalByUsername(un);    // 通过claim中保存的用户名获取到用户的信息
            userOpt.ifPresent(user -> request.setAttribute("user", user));  // 如果该用户存在，则保存在当前Request域中
            if (userOpt.isPresent() && !userOpt.get().getStatus()) {    // 获取status，若为false则返回 "未激活"
                if (!"/user/auth".equals(request.getServletPath())){
                    writeStatusInfo(response);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void writeStatusInfo(HttpServletResponse response) {
        val map = Map.of("status", false, "info", "用户未激活");
        try {
            val str = objectMapper.writeValueAsString(map);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(HttpStatus.LOCKED.value());
            response.getWriter().print(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkJwtToken(HttpServletRequest request) {
        // 获取请求的 Header:Authorization Token进行验证
        val header = request.getHeader(appProperties.getJwt().getHeader());
        // 判断条件【Token不为空 && 以"Bearer"开头】
        return Strings.isNotEmpty(header) && header.startsWith(appProperties.getJwt().getPrefix());
    }

    /**
     * 获取 Token 中的Claims中保存的信息，无则返回 empty()
     */
    private Optional<Claims> validateToken(HttpServletRequest request) {
        // 去掉Token中的 "Bearer" 字段
        val token = request.getHeader(appProperties.getJwt().getHeader()).replace(appProperties.getJwt().getPrefix(), "");
        try {
            // 返回Token中的claims保存的信息
            return Optional.of(Jwts.parserBuilder().setSigningKey(jwtUtil.getKey()).build().parseClaimsJws(token).getBody());
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            //if (e instanceof ExpiredJwtException)
            return Optional.empty();
        }
    }

    private void setSpringAuthentication(Claims claims) {
        // 获取 用户的authorities的List
        val rawList = CollectionUtil.convertObjectToList(claims.get("authorities"));
        val authorities = rawList.stream()
                .map(String::valueOf)    // 将List的元素转成字符串
                .map(SimpleGrantedAuthority::new)   // 用List中的元素 new SimpleGrantedAuthority()
                .collect(Collectors.toList());  // 再转成List  // 到这里即将 List中的所有元素转成 SimpleGrantedAuthority类型，用于保存
        val authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities); // （用户账号，null，用户权限）
        SecurityContextHolder.getContext().setAuthentication(authentication);  // 保存用户的权限
    }
}
