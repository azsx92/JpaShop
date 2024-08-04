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

- Entity, Repository 동작 확인
- jar 빌드해서 동작 확인

### 쿼리 파라미터 로그 남기기

- 로그에 다음을 추가하기: SQL 실행 파라미터를 로그로 남긴다.
- **주의! 스프링 부트 3.x를 사용한다면 영상 내용과 다르기 때문에 다음 내용을 참고하자.**
  - 스프링 부트 2.x, hibernate5 
    - `org.hibernate.type: trace`
    
  - 스프링 부트 3.x, hibernate6 
    -  `org.hibernate.orm.jdbc.bind: trace`
        외부 라이브러리 사용
        https://github.com/gavlyukovskiy/spring-boot-data-source-decorator

스프링 부트를 사용하면 이 라이브러리만 추가하면 된다.
```
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6'
```
참고: 쿼리 파라미터를 로그로 남기는 외부 라이브러리는 시스템 자원을 사용하므로, 개발 단계에서는 편하게 사 용해도 된다. 하지만 운영시스템에 적용하려면 꼭 성능테스트를 하고 사용하는 것이 좋다.
쿼리 파라미터 로그 남기기 - 스프링 부트 3.0
스프링 부트 3.0 이상을 사용하면 라이브러리 버전을 1.9.0 이상을 사용해야 한다.
```
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0'
```
### 애플리케이션 구현 준비
- 구현 요구사항 
- 애플리케이션 아키텍처
![스크린샷 2024-07-31 오후 10.05.20.png](..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fng%2Fx8lr0xx95cl8ffxd1ktw1cf80000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_KFbrmq%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-07-31%20%EC%98%A4%ED%9B%84%2010.05.20.png)

- 회원 기능
  - 회원 등록
  - 회원 조회 
- 상품 기능
  - 상품 등록 
  - 상품 수정
  - 상품 조회 
- 주문 기능
  - 상품 주문
  - 주문 내역 조회 
  - 주문 취소
   
**예제를 단순화 하기 위해 다음 기능은 구현X** 
  - 로그인과 권한 관리X 
  - 파라미터 검증과 예외 처리X 
  - 상품은 도서만 사용 카테고리는 사용X 
  - 배송 정보는 사용X

![스크린샷 2024-07-31 오후 10.07.56.png](..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fng%2Fx8lr0xx95cl8ffxd1ktw1cf80000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_mm1INJ%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-07-31%20%EC%98%A4%ED%9B%84%2010.07.56.png)
**계층형 구조 사용**
- controller, web: 웹 계층
- service: 비즈니스 로직, 트랜잭션 처리
- repository: JPA를 직접 사용하는 계층, 엔티티 매니저 사용 
- domain: 엔티티가 모여 있는 계층, 모든 계층에서 사용

**패키지 구조** 
- jpabook.jpashop 
  - domain 
  - exception 
  - repository 
  - service 
  - web

**개발 순서: 서비스, 리포지토리 계층을 개발하고, 테스트 케이스를 작성해서 검증, 마지막에 웹 계층 적용**

## 변경 감지와 병합(merge)

> 참고: 정말 중요한 내용이니 꼭! 완벽하게 이해하셔야 합니다.

**준영속 엔티티?**

영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말한다.
(여기서는 `itemService.saveItem(book)` 에서 수정을 시도하는 `Book` 객체다. `Book` 객체는 이미 DB에 한번 저장되어서 식별자가 존재한다. 이렇게 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다.)

**준영속 엔티티를 수정하는 2가지 방법** 

- 변경 감지 기능 사용
- 병합( `merge` ) 사용 변경 감지 기능 사용

### 변경 감지 기능 사용
```jave
@Transactional
void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
    Item findItem = em.find(Item.class, itemParam.getId()); //같은 엔티티를 조회한다.
    findItem.setPrice(itemParam.getPrice()); //데이터를 수정한다. 
}
```
영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정하는 방법
트랜잭션 안에서 엔티티를 다시 조회, 변경할 값 선택 트랜잭션 커밋 시점에 변경 감지(Dirty Checking)이 동작해서 데이터베이스에 UPDATE SQL 실행

### 병합 사용
- 병합은 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능이다.
```jave
@Transactional
void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
     Item mergeItem = em.merge(itemParam);
 }
```
![스크린샷 2024-08-04 오후 8.30.05.png](..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fng%2Fx8lr0xx95cl8ffxd1ktw1cf80000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_kdUHdE%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-08-04%20%EC%98%A4%ED%9B%84%208.30.05.png)

**병합 동작 방식**
1. `merge()` 를 실행한다.
2. 파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회한다.
   
2-1. 만약 1차 캐시에 엔티티가 없으면 데이터베이스에서 엔티티를 조회하고, 1차 캐시에 저장한다.
3. 조회한 영속 엔티티( `mergeMember` )에 `member` 엔티티의 값을 채워 넣는다. (member 엔티티의 모든 값을
   mergeMember에 밀어 넣는다. 이때 mergeMember의 “회원1”이라는 이름이 “회원명변경”으로 바뀐다.)
4. 영속 상태인 mergeMember를 반환한다.
  > 참고: 책 자바 ORM 표준 JPA 프로그래밍 3.6.5
   
**병합시 동작 방식을 간단히 정리**
1. 준영속 엔티티의 식별자 값으로 영속 엔티티를 조회한다.
2. 영속 엔티티의 값을 준영속 엔티티의 값으로 모두 교체한다.(병합한다.)
3. 트랜잭션 커밋 시점에 변경 감지 기능이 동작해서 데이터베이스에 UPDATE SQL이 실행

>주의: 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이 변경된 다. 병합시 값이 없으면 `null` 로 업데이트 할 위험도 있다. (병합은 모든 필드를 교체한다.)