package com.example.security01.controller;

import com.example.security01.auth.PrincipalDetails;
import com.example.security01.dto.UserDto;
import com.example.security01.service.IndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {  // DI (의존성 주입)
        log.debug("/test/login ======================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("authentication : {}", principalDetails.getUser());

        log.info("userDetails : {}", userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {  // DI (의존성 주입)
        log.debug("/test/oauth/login ======================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("authentication : {}", oAuth2User.getAttributes());
        log.info("oauth2User : {}", oauth.getAttributes());

        return "OAuth2.0 로그인 세션 정보 확인하기";
    }

    // localhost:8080/
    // localhost:8080
    @GetMapping({"", "/"})
    public String index() {
        // (Spring 에서 권장하는 template engine) Mustache - 기본 폴더 : src/main/resources
        // View Resolver 설정 : templates (prefix), .mustache (suffix) - Mustache 사용 시 생략 가능
        return "index";
    }

    /**
     * 일반 로그인을 해도 PrincipalDetails
     * OAuth2.0 로그인을 해도 PrincipalDetails
     */
    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails : {}", principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(UserDto dto) {
        log.info("UserDto : {}", dto);
        dto.setRole("ROLE_USER");

        String rawPassword = dto.getPassword();
        String encodePassword = bCryptPasswordEncoder.encode(rawPassword);
        dto.setPassword(encodePassword);
        log.info("encodedPassword : {}", dto.getPassword());

        indexService.join(dto);

        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    @ResponseBody
    public String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    @ResponseBody
    public String data() {
        return "데이터 정보";
    }

}
