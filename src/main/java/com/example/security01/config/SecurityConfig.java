package com.example.security01.config;

import com.example.security01.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity  // Spring Security 필터가 Spring 필터체인에 등록됨
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

    // @Bean 사용 시 해당 메서드의 리턴되는 Object 를 IoC 로 등록
    @Bean
    BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    /**
     * OAuth2.0 을 통한 로그인 프로세스
     * 1. 코드 받기 (인증)
     * 2. 엑세스 토큰(코드를 통해 토큰을 받아옴)
     * 3. 사용자 프로필 정보를 가져옴 (토큰을 통해 정보를 조회)
     * 4-1. 가져온 정보를 토대로 회원가입을 자동으로 진행
     * 4-2. 가져온 정보가 부족할 경우, 추가 회원가입 프로세스 진행
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")  // "/login" URL 이 호출이 되면 Security 에서 낚아채서 로그인 진행 처리
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")  // Google 로그인이 완료된 뒤에 후처리 필요 / Tip. 코드 X (엑세스 토큰 + 사용자 프로필 정보)
                .userInfoEndpoint()
                .userService(principalOauth2UserService);  // 후처리 비지니스 로직 구현부
    }

}
