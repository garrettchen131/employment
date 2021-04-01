package cn.sicnu.cs.employment.security.rest;

import cn.sicnu.cs.employment.common.util.JwtUtil;
import cn.sicnu.cs.employment.domain.entity.Role;
import cn.sicnu.cs.employment.domain.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        val user = (User) authentication.getPrincipal();
        jwtUtil.setCookiesForTokens(response, user);  // 返回带有Token的Cookie
        if (user.getStatus()) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            response.setStatus(HttpStatus.LOCKED.value());
        }
        response.setCharacterEncoding("UTF-8");
        Set<Role> authorities = user.getAuthorities();
        String auth = authorities.toString();
        val succData = Map.of(
                "authorities", auth,
                "status", user.getStatus()
        );
        response.getWriter().println(objectMapper.writeValueAsString(succData));
        log.debug("认证成功");
    }
}
