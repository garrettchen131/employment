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
        var claimsOpt = Optional.ofNullable((Claims)null);
        if (checkJwtToken(request)) {
            claimsOpt = validateToken(request);
            claimsOpt.filter(claims -> Objects.nonNull(claims.get("authorities")))
                    .ifPresentOrElse(
                            this::setSpringAuthentication,
                            SecurityContextHolder::clearContext
                    );
        }
        if (claimsOpt.isPresent()){
            val un = claimsOpt.get().getSubject();
            val userOpt = userMapper.findOptionalByUsername(un);
            userOpt.ifPresent(user -> request.setAttribute("user", user));
            if (userOpt.isPresent() && !userOpt.get().getStatus()) {
                writeStatusInfo(response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void writeStatusInfo(HttpServletResponse response) {
        val map = Map.of("status", false, "info", "用户未激活");
        try {
            val str =  objectMapper.writeValueAsString(map);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(HttpStatus.LOCKED.value());
            response.getWriter().print(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkJwtToken(HttpServletRequest request) {
        val header = request.getHeader(appProperties.getJwt().getHeader());
        return Strings.isNotEmpty(header) && header.startsWith(appProperties.getJwt().getPrefix());
    }

    private Optional<Claims> validateToken(HttpServletRequest request) {
        val token = request.getHeader(appProperties.getJwt().getHeader()).replace(appProperties.getJwt().getPrefix(), "");
        try {
            return Optional.of(Jwts.parserBuilder().setSigningKey(jwtUtil.getKey()).build().parseClaimsJws(token).getBody());
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private void setSpringAuthentication(Claims claims) {
        val rawList = CollectionUtil.convertObjectToList(claims.get("authorities"));
        val authorities = rawList.stream()
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        val authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
