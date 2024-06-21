package com.hoangtuyen.profile.configuration;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

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