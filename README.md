# MySns

육아 중인 엄마들이 정보를 공유할 수 있는 위치기반 SNS 개발 프로젝트입니다.

<div>
<img src="./dev_notes/images/스크린샷 2024-04-12 오전 3.35.59.png" width=200 height=250">
<img src="./dev_notes/images/스크린샷 2024-04-12 오전 3.36.16.png" width=200 height=250>
<img src="./dev_notes/images/스크린샷 2024-04-12 오전 3.37.33.png" width=200 height=250>
<img src="./dev_notes/images/스크린샷 2024-04-12 오전 3.36.47.png" width=200 height=250>
<div>

## 기술

- spring
- nextjs
- postgres


## 개발노트
- [가까운 동네 포스트 노출](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md>)
  - [법정동 데이터 저장](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#법정동-데이터-저장>)
  - [Mysql 에서 Postgres 로 디비 변경](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#mysql-에서-postgres-로-디비-변경>)
  - [첫 쿼리 작성](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#첫-쿼리-작성>)
  - [가까운 포스트를 찾는 로직 변경하기](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#가까운-포스트를-찾는-로직-변경하기>)
    - [Spatial Index](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#spatial-index>)
    - [Querydsl-Spatial 적용은 실패](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#querydsl-spatial-적용은-실패>)
  - [Spatial 데이터 유형에 따른 차이 비교](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#spatial-데이터-유형에-따른-차이-비교>)
    - [st\_distance 비교](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#st_distance-비교>)
    - [st\_dwithin 함수 비교](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#st_dwithin-함수-비교>)
  - [검색 조건에 따라 분기 처리 해보기](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#검색-조건에-따라-분기-처리-해보기>)
  - [결론](<dev_notes/가까운 동네의 포스트를 우선적으로 노출.md#결론>)
  
- [N + 1 문제 해결](<dev_notes/N+1 문제해결과 댓글을 3개까지 노출하기.md>)
  - [N + 1 문제](<dev_notes/N+1 문제해결과 댓글을 3개까지 노출하기.md#n--1-문제>)
  - [댓글 가져오기 문제](<dev_notes/N+1 문제해결과 댓글을 3개까지 노출하기.md#댓글-가져오기-문제>)
    - [Java 코드로 해결하기](<dev_notes/N+1 문제해결과 댓글을 3개까지 노출하기.md#java-코드로-해결하기>)
    - [Querydsl Transform 이 순서를 유지하지 않음](<dev_notes/N+1 문제해결과 댓글을 3개까지 노출하기.md##querydsl-transform-이-순서를-유지하지-않음>)
  - [결론](<dev_notes/N+1 문제해결과 댓글을 3개까지 노출하기.md#결론>)
  - [추가: 테스트 코드에서 쿼리가 몇 번 나갔는지 카운팅하기](<dev_notes/N+1 문제해결과 댓글을 3개까지 노출하기.md#추가-테스트-코드에서-쿼리가-몇-번-나갔는지-카운팅하기>)
  
- [파일 업로드](<dev_notes/파일 업로드 개발 과정.md>)
  - [기존 - Spring에서 처리](<dev_notes/파일 업로드 개발 과정.md#기존---spring에서-처리>)
  - [클라이언트에서 바로 s3 로 파일 전송](<dev_notes/파일 업로드 개발 과정.md#클라이언트에서-바로-s3-로-파일-전송>)
    - [CDK](<dev_notes/파일 업로드 개발 과정.md#cdk>)
    - [Lambda Layer](<dev_notes/파일 업로드 개발 과정.md#lambda-layer>)
    - [Lambda 메모리 설정](<dev_notes/파일 업로드 개발 과정.md#lambda-메모리-설정>)
  - [포스트맨 테스트](<dev_notes/파일 업로드 개발 과정.md#포스트맨-테스트>)
  - [CORS](<dev_notes/파일 업로드 개발 과정.md#cors>)
  - [클라이언트 컴포넌트에서 API 요청하기](<dev_notes/파일 업로드 개발 과정.md#클라이언트-컴포넌트에서-api-요청하기>)
  - [Lambda 로 파일 업로드 시 사이즈 제한 문제](<dev_notes/파일 업로드 개발 과정.md#lambda-로-파일-업로드-시-사이즈-제한-문제>)
  - [Presigned Url 을 클라이언트에게 제공](<dev_notes/파일 업로드 개발 과정.md#presigned-url-을-클라이언트에게-제공>)
  - [S3 Event 를 Lambda 로 처리할 때 무한루프를 주의해야 한다](<dev_notes/파일 업로드 개발 과정.md#s3-event-를-lambda-로-처리할-때-무한루프를-주의해야-한다>)
    - [Event Filter 로 해결](<dev_notes/파일 업로드 개발 과정.md#event-filter-로-해결>)
  - [스프링 서버 File Manager](<dev_notes/파일 업로드 개발 과정.md#스프링-서버-file-manager>)
    - [presignGetObject 메서드를 반복문에서 사용해도 문제없는가](<dev_notes/파일 업로드 개발 과정.md#presigngetobject-메서드를-반복문에서-사용해도-문제없는가>)

- [포스트 서비스](<dev_notes/포스트 서비스 개발 과정.md>)
  
- [친구 기능](<dev_notes/친구 기능 개발 과정.md>) 
  - [친구 기능 개발](#친구-기능-개발)
  - [UserFriend 테이블 추가](<dev_notes/친구 기능 개발 과정.md#userfriend-테이블-추가>)
  - [User 테이블](<dev_notes/친구 기능 개발 과정.md#user-테이블>)
  - [UserRepository 업데이트](<dev_notes/친구 기능 개발 과정.md#userrepository-업데이트>)
    - [나와 친구인 사람을 모두 조회하는 메서드](<dev_notes/친구 기능 개발 과정.md#나와-친구인-사람을-모두-조회하는-메서드>)
  - [테스트](<dev_notes/친구 기능 개발 과정.md#테스트>)
    - [Instancio 사용](<dev_notes/친구 기능 개발 과정.md#instancio-사용>)
    - [TestContainer](<dev_notes/친구 기능 개발 과정.md#testcontainer>)
  
- [채팅 기능](<dev_notes/채팅 기능 개발 과정.md>) 
  - [WebSocket](<dev_notes/채팅 기능 개발 과정.md#websocket>)
  - [Stompjs](<dev_notes/채팅 기능 개발 과정.md#stompjs>)
  - [Nextjs 에서 WebSocket을 사용할 수 없음](<dev_notes/채팅 기능 개발 과정.md#nextjs-에서-websocket을-사용할-수-없음>)
  - [프론트 개발](<dev_notes/채팅 기능 개발 과정.md#프론트-개발>)
  - [스프링 개발](<dev_notes/채팅 기능 개발 과정.md#스프링-개발>)
    - [채팅방 Id가 필요함](<dev_notes/채팅 기능 개발 과정.md#채팅방-id가-필요함>)
    - [ChatService](<dev_notes/채팅 기능 개발 과정.md#chatservice>)
    - [ChatController](<dev_notes/채팅 기능 개발 과정.md#chatcontroller>)
  - [테스트](<dev_notes/채팅 기능 개발 과정.md#테스트>)
- [더보기 요청 해결](<dev_notes/Post 더 보기 요청 처리 과정.md>)


## 소스코드
### UserService
- [User.java](/src/main/java/com/mj/mysns/user/entity/User.java) 
- [UserFriend.java](/src/main/java/com/mj/mysns/user/entity/UserFriend.java)
- [UserController.java](/src/main/java/com/mj/mysns/user/controller/UserController.java)
- [UserServiceImpl.java](/src/main/java/com/mj/mysns/user/service/UserServiceImpl.java)
- [CustomizedUserRepositoryImpl.java](/src/main/java/com/mj/mysns/user/repository/CustomizedUserRepositoryImpl.java)
- [CustomizedUserRepositoryImplTest.java](/src/test/java/com/mj/mysns/user/repository/CustomizedUserRepositoryImplTest.java)

### PostService
- [Post.java](/src/main/java/com/mj/mysns/post/entity/Post.java)
- [PostController.java](/src/main/java/com/mj/mysns/post/controller/PostController.java)
- [PostServiceImpl.java](/src/main/java/com/mj/mysns/post/service/PostServiceImpl.java)
- [CustomizedPostRepositoryImpl.java](/src/main/java/com/mj/mysns/post/repository/CustomizedPostRepositoryImpl.java)
- [CustomizedPostRepositoryImplTest.java](/src/test/java/com/mj/mysns/post/repository/CustomizedPostRepositoryImplTest.java)

### ChatService
- [Chat.java](/src/main/java/com/mj/mysns/chat/entity/Chat.java)
- [ChatMessage.java](/src/main/java/com/mj/mysns/chat/entity/ChatMessage.java)
- [ChatUser.java](/src/main/java/com/mj/mysns/chat/entity/ChatUser.java)
- [ChatController.java](/src/main/java/com/mj/mysns/chat/controller/ChatController.java)