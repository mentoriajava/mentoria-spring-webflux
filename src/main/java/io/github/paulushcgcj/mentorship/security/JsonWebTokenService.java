package io.github.paulushcgcj.mentorship.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class JsonWebTokenService {

  private final Key key;
  private final Duration expiration;

  public JsonWebTokenService(
    @Value("${io.github.paulushcgcj.security.secret}") String secret,
    @Value("${io.github.paulushcgcj.security.expire}") Duration expiration
  ) {
    key = Keys.hmacShaKeyFor(hashPassword(secret).getBytes(StandardCharsets.UTF_8));
    this.expiration = expiration;
  }

  public String createJwt(Map<String, Object> claims, String username) {
    return
      Jwts
        .builder()
        .setClaims(claims)
        .setSubject(username)
        .setExpiration(getDate(expiration))
        .setIssuedAt(getDate(null))
        .signWith(key)
        .compact();
  }

  public String createJwt(Authentication authorization) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", authorization.getAuthorities());
    return createJwt(claims, authorization.getName());
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts
      .parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token.replace("Bearer ", StringUtils.EMPTY))
      .getBody();
  }

  public String getUsernameFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public Date getExpirationDateFromToken(String token) {
    return getAllClaimsFromToken(token).getExpiration();
  }

  public List<GrantedAuthority> getRolesFromToken(String token) {
    return AuthorityUtils.commaSeparatedStringToAuthorityList(getAllClaimsFromToken(token).get("roles").toString());
  }

  public Authentication getAuthenticationFromToken(String token) {
    UserDetails userDetails = new User(getUsernameFromToken(token), "", getRolesFromToken(token));
    return new UsernamePasswordAuthenticationToken(userDetails, token, getRolesFromToken(token));
  }

  public boolean validateToken(String token) {
    return getExpirationDateFromToken(token).after(getDate(null));
  }

  private Date getDate(Duration expiration) {
    Instant currentTime = Instant.now();

    if (expiration != null)
      currentTime = currentTime.plus(expiration);

    return Date.from(currentTime);
  }

  @SneakyThrows
  private String hashPassword(String secret) {
    byte[] digestedSecret = MessageDigest.getInstance("SHA-256").digest(secret.getBytes(StandardCharsets.UTF_8));
    String hashedSecret =
      IntStream
        .range(0, digestedSecret.length)
        .mapToObj(index -> Integer.toHexString(digestedSecret[index]))
        .collect(Collectors.joining())
        .toUpperCase();
    return hashedSecret + hashedSecret;
  }


}
