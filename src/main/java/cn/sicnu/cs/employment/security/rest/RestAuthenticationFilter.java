package cn.sicnu.cs.employment.security.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class RestAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 获取传入的username和password
        try (InputStream inputStream = request.getInputStream()) {
            val jsonNode = objectMapper.readTree(inputStream);
            if (!jsonNode.has("username") || !jsonNode.has("password")) {
                throw new BadCredentialsException("用户名参数不正确");
            }
            val username = jsonNode.get("username").textValue();
            val password = jsonNode.get("password").textValue();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password); //TODO：通过账号和密码构造一个Token（和JWTFilter:114有什么区别
            setDetails(request, token);  // TODO：这是在干啥？将token放入Request吗？
            return this.getAuthenticationManager().authenticate(token); // TODO： 进行身份验证然后返回一个认证对象？什么意思
        } catch (IOException e) {
            throw new BadCredentialsException("没有找到用户名或密码参数");
        }
    }
}
