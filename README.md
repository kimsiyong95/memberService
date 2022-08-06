
### Swagger 주소 : <a href="http://localhost:8080/swagger-ui/index.html#/">http://localhost:8080/swagger-ui/index.html#/</a> <br>
##### Swagger 로그인 처리
1. 회원가입 api 이용
2. 로그인 api 이용 후 jwt 발급 후 복사
3. 오른쪽 상단에 있는 Authorize 버튼을 통해 복사한 토큰을 입력 후 전역적인 로그인 처리
4. 그 후 나의 정보 조회 api, 스크랩 조회 api 는 header token 없이 바로 Execute 시 response 조회  

##### 공통설정

* 회원가입, 로그인 기능은 security permitAll 설정
* throw 시 GlobalExceptionHandler 에서 모든 예외 처리 담당
* 내 정보 보기, 유저의 정보 스크랩은 security 인증 체크
  1. jwt filter로 토큰 검증 후 유효하지 않다면 CustomAuthenticationEntryPoint 로 이동
  2. CustomAuthenticationEntryPoint 이동 후 ErrorController 로 response 처리
  3. 유효하다면 UserDetailsService의 구현체인 PrincipalDetailsService에서 loadUserByUsername 구현
  4. 그 후 UserDetails의 구현체인 PrincipalDetails 생성자에 로그인 시킬 사용자 객체를 넘겨준다.
  5. 그 후 SecurityContextHolder 라는 session 에 로그인 한 사용자의 Authentication 객체를 넘김으로 로그인 처리

