package br.com.cooperativevoting.infrastructure.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Loga cada request recebida: método, URI, status de resposta e duração,
 * num único evento por requisição, como um access log mínimo.
 */
@Component
class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long inicio = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duracaoMillis = System.currentTimeMillis() - inicio;
            log.info("{} {} -> {} ({}ms)", request.getMethod(), request.getRequestURI(), response.getStatus(), duracaoMillis);
        }
    }
}
