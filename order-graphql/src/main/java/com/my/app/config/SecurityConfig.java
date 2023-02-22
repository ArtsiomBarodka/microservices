package com.my.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    @Autowired
    private PropertiesConfig propertiesConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(fromJwtToGrantedAuthoritiesConverter());

        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/graphiql/**").permitAll()
                .antMatchers("/graphql/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter).decoder(jwtDecoder());

        return http.build();
    }

    // default algorithm is not working on docker instances
    private JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withJwkSetUri(propertiesConfig.getSecurityJwkSetUri())
                .jwsAlgorithm(SignatureAlgorithm.RS256)
                .build();
    }

    @SuppressWarnings("unchecked")
    private Converter<Jwt, Collection<GrantedAuthority>> fromJwtToGrantedAuthoritiesConverter() {
        return source -> {
            final Map<String, Object> realmAccess = source.getClaim("realm_access");
            if (realmAccess == null || realmAccess.isEmpty()) {
                return new ArrayList<>();
            }

            return ((List<String>) realmAccess.get("roles")).stream()
                    .map(roleName -> "ROLE_" + roleName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        };
    }
}
