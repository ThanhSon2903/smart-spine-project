package DATN.example.demo.utils;

import DATN.example.demo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.antlr.v4.runtime.Token;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {
    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60 * 2;//Thời hạn sống của access_token là 15 phút
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7;//Thời hạn sống của refresh_token 7 ngày
    private final SecretKey secretKey;

   public JwtUtils(){
       String secretString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
       byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
       this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
   }

   public String generateToken(UserDetails userDetails){
        User user = (User) userDetails;
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role",user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
   }

   public String generateRefreshToken(UserDetails userDetails){
       User user = (User) userDetails;
       return Jwts.builder()
               .setSubject(userDetails.getUsername())
               .claim("role",user.getRole().name())
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
               .signWith(secretKey, SignatureAlgorithm.HS256)
               .compact();
   }

   //func de trich xuat thong tin tu token
   public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
   }

   public Date extractExpiration(String token){
       return extractClaim(token, Claims::getExpiration);
   }
   public String extractUsername(String token){
       return extractClaim(token,Claims::getSubject);
   }

   public boolean willExpireSoon(String token,long seconds){
       final Claims claims = Jwts.parserBuilder()
               .setSigningKey(secretKey)
               .build()
               .parseClaimsJws(token)
               .getBody();
       Date expiration = claims.getExpiration();
       return expiration.toInstant()
               .minusSeconds(seconds)
               .isBefore(Instant.now());
   }
   //Xem token con han ko
   public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
   }

   //Xem token co dung khong
   public Boolean isValidToken(String token,UserDetails userDetails){
       final String username = extractUsername(token);
       return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }
}
