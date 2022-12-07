# " 기술 공유에 편리함을 제공하는 Velog In Mobile_Server입니다. "

# 목차
 1. [구성](#1-구성)              
       
 2. [서버 설계](#2-서버-설계)            
       2.1 [데이터 흐름](#21-개발자의-경우설치)                  
       2.2 [Spring Web Layer](#22-Spring-Web-Layer)                   
       2.3 [DB 구조](#23-DB-구조)
       2.4 [보안](#24-보안)

 3. [RestAPI 문서화](#3-RestAPI-문서화)
              
 4. [프로젝트 제작에 참고된 문헌, 서적, URL](#4-프로젝트-제작에-참고된-문헌-서적-url)

 5. [프로젝트 참여 개발자 정보](#5-프로젝트-참여-개발자-정보)

# 1. 구성
Server: Google Cloud

Server Framework: Spring Boot

DB: MySQL

Cloud Messaging: FCM

# 2. 서버 설계
## 2.1 데이터 흐름
![그림01](https://user-images.githubusercontent.com/59440722/206168296-56d02a53-798d-42d5-ba10-800b64480063.png)

VelogInMoblie_Server는 서비스 계층에서 Velog 서버의 RestAPI를 이용해 필요한 데이터를 수집 후 데이터를 가공합니다.

푸시알림은 FCM을 이용합니다.

## 2.2 Spring Web Layer
![그림06](https://user-images.githubusercontent.com/59440722/206169633-8310e3ee-d10f-418e-82f5-f58bbc386125.png)

Spring Web Layer는 위와 같습니다.

## 2.3 DB 구조
![그림02](https://user-images.githubusercontent.com/59440722/206168855-d5ec47d6-a8fd-49c9-8798-752c15240447.png)

주요 테이블로는 User, Target, Subscribe, Tag, Notification이 있습니다.

위와 같은 관계를 가집니다.

![그림03](https://user-images.githubusercontent.com/59440722/206168978-ab21f8b3-5665-477b-886a-b7125d425429.png)

User와 그들이 구독하는 Target(실제 Velog 유저)는 다대다 관계로, 이를 구현하기 위해 User와 Target 테이블은 각각 Subscribe 테이블과 다대일 관계를 맺고있습니다.

## 2.4 보안
Spring Security와 Jwt 토큰을 이용해 API 이용에 대한 인증, 인가처리를 하고있습니다.

# 3. RestAPI 문서화
VelogInMobile_Server는 Swagger를 이용해 자동 문서화를 합니다.

http://localhost:8080/swagger-ui.html

프로젝트를 구동 후 위의 경로에 접속하시면
![그림04](https://user-images.githubusercontent.com/59440722/206170823-e8946504-dd76-4bfa-bfe4-dff85ba30e88.png)

다음과 같은 API 목록들이 생기며 직접 실행해 볼 수 있습니다.

# 4. 프로젝트 제작에 참고된 문헌, 서적, URL

스프링 부트 핵심 가이드 (저자: 장정우, 출판: 위키북스)

스프링 시큐리티
https://twer.tistory.com/entry/Security-스프링-시큐리티의-아키텍처구조-및-흐름

연관관계 매핑
https://gilssang97.tistory.com/46

Jsoup 파싱
https://m.blog.naver.com/lghlove0509/220963952487

FCM 알림
https://firebase.google.com/docs/cloud-messaging/manage-topics?hl=ko
https://backtony.github.io/spring/2021-08-20-spring-fcm-1/
https://velog.io/@co323co/SpringBoot-Push-Server

# 5. 프로젝트 개발자 정보

iOS : 홍준혁 Github Profile - https://github.com/hongjunehuke

iOS : 주현아 Github Profile - https://github.com/JuHyeonAh

Server : 국혜경 Github Profile - https://github.com/k0000k 

Server : 김진수 Github Profile - https://github.com/kikuke
