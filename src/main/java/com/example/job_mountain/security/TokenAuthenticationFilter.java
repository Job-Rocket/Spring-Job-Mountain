package com.example.job_mountain.security;

import com.example.job_mountain.config.AppProperties;
import com.example.job_mountain.validation.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private AppProperties appProperties;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {

            System.out.println("112");
            String jwt = getJwtFromRequest(request);
            System.out.println(getJwtFromRequest(request) + "---------" + LocalDate.now());
            if (StringUtils.hasText(jwt)) {
                System.out.println("11");
                if (tokenProvider.isTokenExpired(jwt)) {
                    createResponse(ExceptionCode.EXPIRED_JWT_TOKEN, response);
                } else {
                    System.out.println("112");
                    UserDetails userDetails = null;
                    System.out.println(request.getRequestURI());

                    // 토큰 확인 url
                    if (request.getRequestURI().startsWith("/user") || request.getRequestURI().startsWith("/company")) {
                        System.out.println("113");
                        Long userId = tokenProvider.getUserIdFromToken(jwt);
                        userDetails = customUserDetailsService.loadUserById(userId);
                    } else {
                        System.out.println("114");
                    }

                    if (userDetails == null) {
                        createResponse(ExceptionCode.INVALID_JWT_TOKEN, response);
                        return;
                    }

                    System.out.println("115");
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                            userDetails.getPassword(), userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("116");
                    System.out.println("TokenAuthenticationFilter.doFilterInternal---------");
                    filterChain.doFilter(request, response);
                }
            } else {
                filterChain.doFilter(request, response);
            }

        }
//        catch (SignatureException ex) {
//            logger.error("Invalid JWT signature");
//            createResponse(ExceptionCode.INVALID_JWT_SIGNATURE, response);
//        }
          catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
            createResponse(ExceptionCode.INVALID_JWT_TOKEN, response);
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
            createResponse(ExceptionCode.EXPIRED_JWT_TOKEN, response);
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
            createResponse(ExceptionCode.UNSUPPORTED_JWT_TOKEN, response);
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void createResponse(ExceptionCode exceptionCode, HttpServletResponse response) throws IOException {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("state", HttpServletResponse.SC_UNAUTHORIZED);
        json.put("code", exceptionCode.getCode());

        json.put("message", exceptionCode.getMessage());

        String newResponse = new ObjectMapper().writeValueAsString(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setContentLength(newResponse.getBytes(StandardCharsets.UTF_8).length);
        response.getWriter().write(newResponse);
    }
}
