package cn.sicnu.cs.employment.security.rest;

import cn.sicnu.cs.employment.common.util.JwtUtil;
import cn.sicnu.cs.employment.domain.entity.User;
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
@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        val user = (User) authentication.getPrincipal();
        jwtUtil.setCookiesForTokens(response, user);  // 返回带有Token的Cookie
        if (user.getStatus()) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            response.setStatus(HttpStatus.LOCKED.value());
        }
        response.getWriter().println();
        log.debug("认证成功");
    }
}
