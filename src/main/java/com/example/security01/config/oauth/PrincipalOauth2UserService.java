package com.example.security01.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // Google 로 부터 받은 userRequest 데이터에 대한 후처리 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getClientRegistration: {}", userRequest.getClientRegistration());
        log.info("getAccessToken: {}", userRequest.getAccessToken().getTokenValue());
        log.info("getAttributes: {}", super.loadUser(userRequest).getAttributes());
        return super.loadUser(userRequest);
    }

}
