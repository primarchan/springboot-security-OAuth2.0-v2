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
- Facebook
  - [공식 문서 링크](https://developers.facebook.com/docs/facebook-login/web)