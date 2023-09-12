package com.example.user.service.impl;

import com.example.user.client.CloudAuthClient;
import com.example.user.model.OAuthTokenDTO;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * 用户相关
 *
 * @author minus
 * @since 2023/9/12 16:21
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CloudAuthClient cloudAuthClient;

    @Value("${auth.client-id}")
    private String clientId;

    @Value("${auth.client-secret}")
    private String clientSecret;

    @Override
    public OAuthTokenDTO login(String username, String password) {
        String authorization = "Basic " + Base64.getEncoder().encodeToString((clientId + ':' + clientSecret).getBytes());
        OAuthTokenDTO oAuthTokenDTO = cloudAuthClient.getToken(authorization, "password", username, password);
        SecurityContextHolder.getContext().getAuthentication();
        return oAuthTokenDTO;
    }

}
