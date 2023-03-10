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
        log.info("getClientRegistration: {}", userRequest.getClientRegistration());  // registrationId 로 어떤 OAuth 로 로그인 했는지 확인 가능
        log.info("getAccessToken: {}", userRequest.getAccessToken().getTokenValue());

        // Google 로그인 버튼 클릭 -> Google 로그인 화면 -> 로그인 완료 -> code 리턴 (OAuth-Client 라이브러리) -> Access Token 요청
        // userRequest 정보 -> loadUser 메서드 호출 -> Google 로 부터 회원 프로필 받아줌
        log.info("getAttributes: {}", super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        return super.loadUser(userRequest);
    }

}
