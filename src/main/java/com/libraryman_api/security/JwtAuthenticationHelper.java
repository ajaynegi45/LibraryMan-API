package com.libraryman_api.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtAuthenticationHelper {

	private static final long JWT_TOKEN_VALIDITY=60*60;
	private String secret="thisisacodingninjasdemonstrationforsecretkeyinspringsecurityjsonwebtokenauthentication";
	public String getUsernameFromToken(String token) {
		String username=getClaimsFromToken(token).getSubject();
		return username;
	}
	
	public Claims getClaimsFromToken(String token) {
		Claims claims=Jwts.parserBuilder()
				.setSigningKey(secret.getBytes())
				.build().parseClaimsJws(token).getBody();
		return claims;
	}
	
	public Boolean isTokenExpired(String token) {
		Claims claims=getClaimsFromToken(token);
		Date expDate=claims.getExpiration();
		return expDate.before(new Date());
	}
	
	public String generateToken(UserDetails userDetails) {
		Map<String,Object> claims=new HashMap<>();
		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
		.setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000))
		.signWith(new SecretKeySpec(secret.getBytes(),SignatureAlgorithm.HS512.getJcaName()),SignatureAlgorithm.HS512)
		.compact();
	}
}
