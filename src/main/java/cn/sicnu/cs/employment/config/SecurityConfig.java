package cn.sicnu.cs.employment.config;

import cn.sicnu.cs.employment.common.util.JwtUtil;
import cn.sicnu.cs.employment.exception.SimpleAuthenticationEntryPoint;
import cn.sicnu.cs.employment.security.jwt.JwtFilter;
import cn.sicnu.cs.employment.security.rest.RestAuthenticationFailureHandler;
import cn.sicnu.cs.employment.security.rest.RestAuthenticationFilter;
import cn.sicnu.cs.employment.security.rest.RestAuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Arrays;
import java.util.Collections;

import static cn.sicnu.cs.employment.common.Constants.ROLE_ENTERPRISE_SUPER;
import static cn.sicnu.cs.employment.common.Constants.ROLE_UNIVERSITY_SUPER;

@Configuration
@RequiredArgsConstructor
@Import(SecurityProblemSupport.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProblemSupport problemSupport;
    private final Environment environment;
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(problemSupport)
                        .accessDeniedHandler(problemSupport))
                .authorizeRequests(authentication ->
                        authentication
                                .mvcMatchers("/", "/auth/**").permitAll()
                                .mvcMatchers("/admin/**").hasAnyAuthority(ROLE_UNIVERSITY_SUPER, ROLE_ENTERPRISE_SUPER)

                                .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new SimpleAuthenticationEntryPoint());

    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/public/**", "/h2/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestAuthenticationFilter restAuthenticationFilter() throws Exception {
        val filter = new RestAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(new RestAuthenticationSuccessHandler(jwtUtil, objectMapper));
        filter.setAuthenticationFailureHandler(new RestAuthenticationFailureHandler(objectMapper));
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/login");
        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许跨域访问的主机
//        if (environment.acceptsProfiles(Profiles.of("dev"))) {
//            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4001"));
//        } else {
//            configuration.setAllowedOrigins(Collections.singletonList("https://uaa.imooc.com"));
//        }
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.addExposedHeader("X-Authenticate");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
