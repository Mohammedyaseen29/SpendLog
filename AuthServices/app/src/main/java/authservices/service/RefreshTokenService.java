package authservices.service;


import authservices.Repository.RefreshTokenRepository;
import authservices.Repository.UserRepository;
import authservices.entities.Token;
import authservices.entities.UserInfo;
import org.modelmapper.internal.bytebuddy.dynamic.DynamicType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshToken;

    @Autowired
    UserRepository user;

    public Token createRefreshToken(String username){
        UserInfo userInfoExtracted = user.findByUserName(username);
        Token token = Token.builder().userInfo(userInfoExtracted).token(UUID.randomUUID().toString()).expiry_date(Instant.now().plusMillis(600000)).build();
        return token;
    }

    public Optional<Token> findByToken(String token){
        return refreshToken.findByToken(token);
    }

    public Token verifyExpiration(Token token){
        if(token.getExpiry_date().compareTo(Instant.now())<0){
            refreshToken.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
