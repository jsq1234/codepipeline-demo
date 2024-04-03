package library.backend.api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class JwtUtils {

    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    private JwtUtils() {
    }

    public static boolean validateToken(String jwtToken) {
        return parseToken(jwtToken) != null;
    }

    public static String getEmailFromToken(String jwtToken) {
        Claims claims = parseToken(jwtToken);
        if (claims != null) {
            return claims.getSubject();
        }

        return null;
    }

    private static Claims parseToken(String jwtToken) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        try {
            return jwtParser.parseSignedClaims(jwtToken)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
        }

        return null;
    }

    public static String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .signWith(secretKey)
                .compact();
    }
}
