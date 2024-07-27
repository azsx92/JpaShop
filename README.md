# 김영한님의 실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발

### 1. 프로젝트 환경설정
```
- 스프링 부트 스타터(https://start.spring.io/)
- Project: **Gradle - Groovy** Project
- 사용 기능: web, thymeleaf, jpa, h2, lombok, validation
- groupId: jpabook artifactId: jpashop

**필독! 주의!**
강의 영상이 JUnit4를 기준으로 하기 때문에 `build.gradle` 에 있는 다음 부분을 꼭 직접 추가해주세요.
해당 부분을 입력하지 않으면 JUnit5로 동작합니다. JUnit5를 잘 알고 선호하시면 입력하지 않아도 됩니다.

//JUnit4 추가
testImplementation("org.junit.vintage:junit-vintage-engine") {
    exclude group: "org.hamcrest", module: "hamcrest-core"
}
```
2. 롬복적용
```
롬복 적용
1. Preferences plugin lombok 검색 실행 (재시작)
2. Preferences Annotation Processors 검색 Enable annotation processing 체크 (재시작)
3. 임의의 테스트 클래스를 만들고 @Getter, @Setter 확인
```
3.View 환경설정
- thymeleaf 공식 사이트: https://www.thymeleaf.org/
- 스프링 공식 튜토리얼: https://spring.io/guides/gs/serving-web-content/
- 스프링부트 메뉴얼: https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/ boot-features-developing-web-applications.html#boot-features-spring-mvc-template-engines

4.H2 데이터베이스 설치
개발이나 테스트 용도로 가볍고 편리한 DB, 웹 화면 제공
[https://www.h2database.com]()
- 다운로드 및 설치
  - 스프링 부트 2.x를 사용하면 **1.4.200 버전**을 다운로드 받으면 된다.
  - 스프링 부트 3.x를 사용하면 **2.1.214 버전 이상** 사용해야 한다. 
- 데이터베이스 파일 생성 방법
  - `jdbc:h2:~/jpashop` (최소 한번)
  - `~/jpashop.mv.db` 파일 생성 확인 
  - 이후 부터는 `jdbc:h2:tcp://localhost/~/jpashop` 이렇게 접속
5. yml 띄어쓰기 주의
```yml
spring: #띄어쓰기 없음 
  datasource: #띄어쓰기 2칸
   url: jdbc:h2:tcp://localhost/~/jpashop #4칸 username: sa
   password:
   driver-class-name: org.h2.Driver

  jpa: #띄어쓰기 2칸 
    hibernate: #띄어쓰기 4칸
      ddl-auto: create #띄어쓰기 6칸 
    properties: #띄어쓰기 4칸
      hibernate: #띄어쓰기 6칸 
#       show_sql: true #띄어쓰기 8칸
        format_sql: true #띄어쓰기 8칸

logging.level: #띄어쓰기 없음 
  org.hibernate.SQL: debug #띄어쓰기 2칸
# org.hibernate.type: trace #띄어쓰기 2칸
```