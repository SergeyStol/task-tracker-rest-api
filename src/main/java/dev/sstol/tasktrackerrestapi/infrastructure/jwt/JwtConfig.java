package dev.sstol.tasktrackerrestapi.infrastructure.jwt;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Sergey Stol
 * 2024-10-09
 */
@Configuration
public class JwtConfig {
   @Value("${jwt.public.key}")
   RSAPublicKey rsaPublicKey;

   @Value("${jwt.private.key}")
   RSAPrivateKey rsaPrivateKey;


   @Bean
   JwtDecoder jwtDecoder() {
      return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
   }

   @Bean
   JwtEncoder jwtEncoder() {
      JWK jwk = new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).build();
      JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
      return new NimbusJwtEncoder(jwks);
   }
}
