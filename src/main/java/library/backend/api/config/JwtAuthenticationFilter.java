package library.backend.api.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import library.backend.api.services.EmailDetailsServiceImpl;
import library.backend.api.utils.JwtUtils;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final EmailDetailsServiceImpl emailDetailsService;

    public JwtAuthenticationFilter(EmailDetailsServiceImpl emailDetailsService) {
        this.emailDetailsService = emailDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get token from request
        var jwtToken = getTokenFromRequest(request);

        // Validate jwt tokens
        if (jwtToken != null) {
            if (JwtUtils.validateToken(jwtToken)) {
                // Get username from token
                String email = JwtUtils.getEmailFromToken(jwtToken);

                // fetch user details from email
                UserDetails userDetails = emailDetailsService.loadUserByUsername(email);

                // Create Authentication Token
                var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());

                // Set authentication token to security context holder
                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // Extract authentication header
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isEmpty()) {
            return null;
        }

        // Bearer <JWT TOKEN>
        if (authHeader.length() > 7 && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

}
