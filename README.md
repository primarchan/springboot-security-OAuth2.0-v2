# Spring Security Auth2.0 V2

## Spring Security basic V1 참고
- https://github.com/primarchan/springboot-security-basic-v1
- Spring Security
  - `Session` 내부에 `Security Session` 존재
  - `Security Session` 내부에는 `Authentication` 타입의 객체만 가능 (`Controller` 에서 해당 객체를 `DI`)
  - `Authentication` 객체 내부에는 `UserDetails`, `OAuth2User` 타입 가능
  - 일반적인 로그인 시 `UserDetails` 타입, OAuth 로그인(Google, Facebook 등) 시 `OAuth2User` 타입
  - 위와 같이 로그인 방법에 따라 불필요한 분기가 발생 -> `UserDetails`, `OAuth2User` 를 implement 한 `PrincipalDetails` 를 사용

## OAuth2.0 로그인 가이드
- Google
  - [공식 문서 링크]()
- Facebook
  - [공식 문서 링크](https://developers.facebook.com/docs/facebook-login/web)
- Naver
  - Naver 의 경우 `OAuth2.0` 에서 `Provider` 로 지원하지 않으므로 별도로 설정
  - [공식 문서 링크](https://developers.naver.com/docs/login/devguide/devguide.md)

## application.yml 예시
```yaml
server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      charset: UTF-8
      enabled: true
      force: true

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/security?serverTimezone=Asia/Seoul
    username: *****
    password: *****

  mvc:
    view:
      prefix: /templates/
      suffix: .mustache

  jpa:
    hibernate:
      ddl-auto: update #create update none
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: true

  security:
    oauth2:
      client:
        registration:
          google: # /oauth2/authorization/google 이 주소를 동작하게 한다.
            client-id: *****
            client-secret: *****
            scope:
              - email
              - profile

          facebook:
            client-id: *****
            client-secret: *****
            scope:
              - email
              - public_profile

          # 네이버는 OAuth2.0 공식 지원대상이 아니라서 provider 설정이 필요하다.
          # 요청주소도 다르고, 응답 데이터도 다르기 때문이다.
          naver:
            client-id: *****
            client-secret: *****
            scope:
              - name
              - email
              - profile_image
            client-name: Naver # 클라이언트 네임은 구글 페이스북도 대문자로 시작하더라.
            authorization-grant-type: authorization_code
            redirect-uri: http://localhost:8080/login/oauth2/code/naver

        provider:
          naver:
            authorization-uri: https://nid.naver.com/oauth2.0/authorize
            token-uri: https://nid.naver.com/oauth2.0/token
            user-info-uri: https://openapi.naver.com/v1/nid/me
            user-name-attribute: response # 회원정보를 json의 response 키값으로 리턴해줌.
```