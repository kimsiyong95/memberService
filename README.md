
### Swagger 주소 : <a href="http://localhost:8080/swagger-ui/index.html#/">http://localhost:8080/swagger-ui/index.html#/</a> <br>
##### Swagger 로그인 처리
1. 회원가입 api 이용
2. 로그인 api 이용 후 jwt 발급 후 복사
3. 오른쪽 상단에 있는 Authorize 버튼을 통해 복사한 토큰을 입력 후 전역적인 로그인 처리
4. 그 후 나의 정보 조회 api, 스크랩 조회 api 는 header token 없이 바로 Execute 시 response 조회  

### 요구사항 구현여부 및 구현방법

##### 공통설정

* 회원가입, 로그인 기능은 security permitAll 설정
* throw 시 GlobalExceptionHandler 에서 모든 예외 처리 담당
* 내 정보 보기, 유저의 정보 스크랩은 security 인증 체크
  1. jwt filter로 토큰 검증 후 유효하지 않다면 CustomAuthenticationEntryPoint 로 이동
  2. CustomAuthenticationEntryPoint 이동 후 ErrorController 로 response 처리
  3. 유효하다면 UserDetailsService의 구현체인 PrincipalDetailsService에서 loadUserByUsername 구현
  4. 그 후 UserDetails의 구현체인 PrincipalDetails 생성자에 로그인 시킬 사용자 객체를 넘겨준다.
  5. 그 후 SecurityContextHolder 라는 session 에 로그인 한 사용자의 Authentication 객체를 넘김으로 로그인 처리

##### 회원가입 (O)

1. MemberSignUpRequestDTO @valid 설정으로 빈 값이 올 경우 throw 처리
2. 제공해주신 홍길동, 손오공, 베지터 등의 사람이 아닐 경우 throw 처리
3. 이미 가입되어 있는지 체크 후 가입되어 있다면 throw 처리
4. 주민번호(AES256), 비밀번호(PasswordEncoder) 암호화 사용
5. 모든 과정을 통과하면 회원가입 처리

##### 로그인 (O)

1. MemberLoginRequestDTO @valid 설정으로 빈 값이 올 경우 throw 처리
2. 입력한 id, password 검증 후 검색 된 회원이 없으면 throw 처리
3. 검색된 회원이 id로 토큰 제목 설정 후 서버에 있는 개인키를 base64 encoding 후 HS256 방식으로 header와 payload를 <br>
   암호화 한 데이터로 signature 생성 후 jwt 발급 


##### 내 정보 보기 (O)

1. 로그인이 성공적으로 처리가 되었다면 Principal.getName 으로 가져온 id로 사용자의 정보 response 처리   

##### 유저의 정보를 스크랩(O)

1. 로그인이 성공적으로 처리가 되었다면 사용자 정보를 조회
2. WebClient를 이용하여 https://codetest.3o3.co.kr/v1/scrap 에서 사용자 정보 스크랩 데이터를 Map 형식으로 return 받음
3. 도중에 error 발생시 throw 처리
4. 성공적으로 데이터를 가져오면 ObjectMapper를 이용하여 map 데이터를 json string 형식으로 scrap 컬럼에 저장
5. 사용자에게 조회한 스크랩 데이터를 response 처리

##### 환급액 조회 (O)
1. 로그인이 성공적으로 처리가 되었다면 사용자 정보를 조회
2. scrap 데이터가 json string 형식으로 되어있어 gson 을 사용해 jsonObject로 변환
3. 그 후에 제공해주신 공식을 사용해서 사용자 환급액 계산 후 response 처리