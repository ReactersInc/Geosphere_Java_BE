package com.tridev.geoSphere.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tridev.geoSphere.dto.common.ErrorResponse;

import com.tridev.geoSphere.utils.UserServiceDetailsImpl;
import com.tridev.geoSphere.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/auth",
            "/login",
            "/actuator/health",
            "/v3/api-docs/",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/error",
            "/ws",
            "/ws/**"
    );

    private final UserServiceDetailsImpl userServiceDetails;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {

        log.info("the uri is {}", request.getRequestURI());


        try {
            if (shouldNotFilter(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String authorizationHeader = request.getHeader("Authorization");


            log.info("the auth is {}", authorizationHeader);

            if (authorizationHeader == null) {
                throw new JwtAuthenticationException(HttpStatus.UNAUTHORIZED,
                        "Authorization header missing - Please provide a Bearer token");
            }

            if (!authorizationHeader.startsWith("Bearer ")) {
                throw new JwtAuthenticationException(HttpStatus.UNAUTHORIZED,
                        "Invalid Authorization header format - Expected 'Bearer <token>'");
            }



            validateAuthorizationHeader(authorizationHeader);


            String jwt = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(jwt);

            log.info("username is {}", username);

            log.info("security context {}", SecurityContextHolder.getContext().getAuthentication());

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(request, jwt, username);
            }

            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            throw ex;
        } catch (JwtAuthenticationException ex) {
            log.error("Invalid or Expired Token", ex);
            SecurityContextHolder.clearContext();
            sendErrorResponse(request,response, ex.getStatus(), ex.getMessage());

        } catch (Exception ex) {
            log.error("Unexpected error during JWT authentication", ex);
            SecurityContextHolder.clearContext();

            sendErrorResponse(request,response, HttpStatus.UNAUTHORIZED, "Invalid Token");

            sendErrorResponse(request,response, HttpStatus.UNAUTHORIZED, "Invalid or expired token");

        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDED_PATHS.stream().anyMatch(path ->
                request.getRequestURI().contains(path) ||
                        request.getRequestURI().equals(path.replace("/", "")));
    }

    private void validateAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new JwtAuthenticationException(
                    HttpStatus.UNAUTHORIZED,
                    "Authorization header with Bearer token required"
            );
        }
    }

    private void authenticateUser(HttpServletRequest request, String jwt, String username) {
        UserDetails userDetails = userServiceDetails.loadUserByUsername(username);

        if (jwtUtil.validateToken(jwt)) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            jwt,
                            userDetails.getAuthorities()
                    );
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authenticated user: {}", username);
            log.info("Authenticated Context: {}", SecurityContextHolder.getContext().getAuthentication());

        } else {
            throw new JwtAuthenticationException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or expired JWT token"
            );
        }
    }

    private void sendErrorResponse(HttpServletRequest request,HttpServletResponse response,
                                   HttpStatus status,
                                   String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    public static class JwtAuthenticationException extends RuntimeException {
        private final HttpStatus status;

        public JwtAuthenticationException(HttpStatus status, String message) {
            super(message);
            this.status = status;
        }

        public HttpStatus getStatus() {
            return status;
        }
    }
}