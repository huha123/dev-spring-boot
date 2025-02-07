package dev.huha123.app.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SecurityAuthEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.info("##### SecurityAuthEntryPointHandler authException.getMessage():{}", authException.getMessage());
        log.info("##### SecurityAuthEntryPointHandler request.getRequestURL():{}", request.getRequestURL());
        /* http code 401 handler */
    }

}
