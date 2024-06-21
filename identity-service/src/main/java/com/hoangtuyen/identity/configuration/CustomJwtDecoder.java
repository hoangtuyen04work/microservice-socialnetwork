package com.hoangtuyen.identity.configuration;

import com.hoangtuyen.identity.service.AuthenticationService;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.JwtDecoder;


import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJwt = SignedJWT.parse(token);
            return new Jwt(token,
                    signedJwt.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJwt.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJwt.getHeader().toJSONObject(),
                    signedJwt.getJWTClaimsSet().getClaims()
                    );
        } catch (ParseException e) {
            throw new JwtException("Invalid token Exeption");
        }
    }





}