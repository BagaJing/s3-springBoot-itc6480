package com.awsdemo.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class tokenCheckService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    public boolean isValidToken(String token){
        String aws_cognito_region = "us-east-1";
        RSAKeyProvider keyProvider = new AwsCognitoRsaKeyProvider(aws_cognito_region,"us-east-1_Y2PgefGKO");
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            jwtVerifier.verify(token);
            logger.info("Verfiy Succeed");
            return true;
        } catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

    }
}
