package com.sidramesh.restapidemo.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {
    private final byte[] secret;
    private final String issuer;
    private final long expirationSeconds;

    public JwtUtil(@Value("${app.jwt.secret}") String secret,
                   @Value("${app.jwt.issuer}") String issuer,
                   @Value("${app.jwt.expiration-minutes}") long minutes) {
        this.secret = secret.getBytes();
        this.issuer = issuer;
        this.expirationSeconds = minutes * 60;
    }

    public String generateToken(String internalUserId) throws Exception {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .subject(internalUserId)
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plusSeconds(expirationSeconds)))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        JWSSigner signer = new MACSigner(secret);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    public JWTClaimsSet validate(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(secret);
        if (!signedJWT.verify(verifier)) throw new JOSEException("Invalid signature");
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        // Optionally validate issuer, expiration
        return claims;
    }
}
