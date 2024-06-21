package com.hoangtuyen.identity.service;

import com.hoangtuyen.identity.dto.CreateTokenObject;
import com.hoangtuyen.identity.entity.RoleEntity;
import com.hoangtuyen.identity.exception.AppException;
import com.hoangtuyen.identity.exception.ErrorCode;
import com.hoangtuyen.identity.repository.InvalidatedTokenRepository;
import com.hoangtuyen.identity.entity.InvalidatedTokenEntity;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
public class TokenService {
    InvalidatedTokenRepository tokenRepository;
    @Value("${jwt.signerKey}")
    @NonFinal
    String SIGNER_KEY;

    public InvalidatedTokenEntity save(InvalidatedTokenEntity invalidatedToken) {
        return tokenRepository.save(invalidatedToken);
    }

    public void unableToken(String token) throws AppException, ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(token);
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedTokenEntity invalidatedToken =
                InvalidatedTokenEntity.builder().id(jit).expiryTime(expiryTime).build();
        save(invalidatedToken);
    }
    private boolean isValidJWT(String token) {
        if (token == null || token.split("\\.").length != 3) {
            return false;
        }
        try {
            String header = new String(java.util.Base64.getUrlDecoder().decode(token.split("\\.")[0]));
            JWSObject.parse(header);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException, AppException {
        if (!isValidJWT(token)) {
            throw new AppException(ErrorCode.NOT_AUTHENTICATED);
        }
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.NOT_AUTHENTICATED);
        }
        return signedJWT;
    }
    public  String generateToken(CreateTokenObject object, boolean isRefresh) throws JOSEException{
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        String roleScope = buildRole(object.getRoles());
        Date expiryTime;
        if (isRefresh) {
            expiryTime = Date.from(Instant.now().plus(100, ChronoUnit.HOURS));
        } else {
            expiryTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        }
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(object.getId())
                .issuer("hoangtuyen.com")
                .issueTime(new Date())
                .expirationTime(expiryTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("name", object.getName())
                .claim("scope", roleScope)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return jwsObject.serialize();
    }

    public  String buildRole(Set<RoleEntity> roleEntities){
        StringJoiner joiner = new StringJoiner(" ");
        for(RoleEntity roleEntity : roleEntities){
            joiner.add("ROLE_" + roleEntity.getRole());
        }
        return joiner.toString();
    }


}
