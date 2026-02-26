package com.nhiennhatt.bookstoreapi.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Base64;

public class JwtUtil {
    private final String accessTokenPublicKeyStr;
    private final String accessTokenPrivateKeyStr;
    private final TemporalAmount expiresIn;

    public JwtUtil(String accessTokenPublicKeyStr, String accessTokenPrivateKeyStr, TemporalAmount expiresIn) {
        this.accessTokenPublicKeyStr = accessTokenPublicKeyStr;
        this.accessTokenPrivateKeyStr = accessTokenPrivateKeyStr;
        this.expiresIn = expiresIn;
    }

    private RSAPrivateKey loadPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(accessTokenPrivateKeyStr);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(pkcs8KeySpec);
    }

    private RSAPublicKey loadPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(accessTokenPublicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    public String generateToken(String username) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Algorithm algorithm = Algorithm.RSA256(loadPublicKey(), loadPrivateKey());

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(Instant.now().plus(expiresIn))
                .sign(algorithm);
    }

    public String verify(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256(loadPublicKey(), loadPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
