package dev.huha123.app.config;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("##### SecurityAccessDeniedHandler authException.getMessage():{}", accessDeniedException.getMessage());
        log.info("##### SecurityAccessDeniedHandler request.getRequestURL():{}", request.getRequestURL());
        /* http code 403 handler  */
    }

}
