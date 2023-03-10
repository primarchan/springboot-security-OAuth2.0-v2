package com.example.security01.config.oauth;

import com.example.security01.auth.PrincipalDetails;
import com.example.security01.config.oauth.provider.FacebookUserInfo;
import com.example.security01.config.oauth.provider.GoogleUserInfo;
import com.example.security01.config.oauth.provider.NaverUserInfo;
import com.example.security01.config.oauth.provider.OAuth2UserInfo;
import com.example.security01.model.User;
import com.example.security01.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    // Google 로 부터 받은 userRequest 데이터에 대한 후처리 메서드
    // 메서드 종료 시 @AuthenticationPrincipal 어노테이션인 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getClientRegistration: {}", userRequest.getClientRegistration());  // registrationId 로 어떤 OAuth 로 로그인 했는지 확인 가능
        log.info("getAccessToken: {}", userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // Google 로그인 버튼 클릭 -> Google 로그인 화면 -> 로그인 완료 -> code 리턴 (OAuth-Client 라이브러리) -> Access Token 요청
        // userRequest 정보 -> loadUser 메서드 호출 -> Google 로 부터 회원 프로필 받아줌
        log.info("getAttributes: {}", oAuth2User.getAttributes());

        // OAuth2.0 - Google 로그인 회원 정보를 토대로 자동 회원가입 진행
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("Google 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            log.info("Facebook 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.info("Naver 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else {
            log.info("Google, Facebook, Naver 로그인만 지원합니다.");
        }

        /*
        String provider = userRequest.getClientRegistration().getRegistrationId();  // google
        String providerId = oAuth2User.getAttribute("sub");  // 109742856182916427686
        String username = provider + "_" + providerId;  // google_109742856182916427686
        String password = bCryptPasswordEncoder.encode(userRequest.getClientRegistration().getClientId());
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";
         */

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId();
        String password = bCryptPasswordEncoder.encode(userRequest.getClientRegistration().getClientId());
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());  // Authentication 객체로 반환
    }

}
