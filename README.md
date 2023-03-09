# Spring Security Basic V1

## MySQL DB 및 사용자 생성
````sql
create user 'cos'@'%' identified by 'cos1234';
GRANT ALL PRIVILEGES ON *.* TO 'cos'@'%';
create database security;
use security;
````

## SecurityConfig.java 권한 설정 방법
```java
// protected void configure(HttpSecurity http) Method 내부에 권한 설정법
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
```

## Controller 의 Method 에 직접 권한 설정 하는 방법
```java
// 특정 주소 접근시 권한 및 인증을 위한 Annotation 활성화 SecurityConfig.java 에 설정
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)

// Controller 에 Annotation 거는 법
@PostAuthorize("hasRole('ROLE_MANAGER')")
@PreAuthorize("hasRole('ROLE_MANAGER')")
@Secured("ROLE_MANAGER")
```