# 2D 스타일 SRPG 웹게임 배포 및 실행 가이드

## 1. 프로젝트 개요

이 프로젝트는 스프링부트와 Mustache 프레임워크를 사용하여 개발된 2D 스타일 SRPG(Strategy Role-Playing Game) 웹게임입니다. 그리드 기반 맵 시스템, 턴 기반 전투, 캐릭터 스탯 및 진행 시스템 등 SRPG 장르의 핵심 요소를 구현했습니다.

## 2. 기술 스택

- **백엔드**: Spring Boot 2.7.x
- **프론트엔드**: HTML, CSS, JavaScript
- **템플릿 엔진**: Mustache
- **통신**: RESTful API, WebSocket

## 3. 프로젝트 구조

```
srpg-game/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── game/
│   │   │           └── srpg/
│   │   │               ├── config/
│   │   │               ├── controller/
│   │   │               ├── model/
│   │   │               ├── service/
│   │   │               └── SrpgGameApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   └── js/
│   │       └── templates/
└── pom.xml
```

## 4. 설치 및 실행 방법

### 4.1 필수 요구사항

- Java 17 이상
- Maven 3.6 이상

### 4.2 프로젝트 설치

1. 프로젝트 패키지를 다운로드하고 압축을 해제합니다.
2. 터미널/명령 프롬프트를 열고 프로젝트 루트 디렉토리로 이동합니다.

### 4.3 프로젝트 실행

#### Maven을 사용한 실행

```bash
cd srpg-game
mvn spring-boot:run
```

#### JAR 파일 생성 후 실행

```bash
cd srpg-game
mvn clean package
java -jar target/srpg-game-0.0.1-SNAPSHOT.jar
```

### 4.4 애플리케이션 접속

웹 브라우저에서 다음 URL로 접속합니다:
- 메인 페이지: http://localhost:8080
- 게임 페이지: http://localhost:8080/game
- 테스트 페이지: http://localhost:8080/test

## 5. 게임 플레이 방법

### 5.1 게임 시작

1. 메인 페이지에서 "게임 시작" 버튼을 클릭합니다.
2. 게임 페이지가 로드되면 자동으로 새 게임이 시작됩니다.

### 5.2 게임 조작

1. **유닛 선택**: 게임 보드에서 유닛을 클릭하여 선택합니다.
2. **이동**: 선택한 유닛으로 "이동" 버튼을 클릭한 후, 이동 가능한 타일(녹색 하이라이트)을 클릭합니다.
3. **공격**: 선택한 유닛으로 "공격" 버튼을 클릭한 후, 공격 범위 내의 적 유닛(빨간색 하이라이트)을 클릭합니다.
4. **턴 종료**: 모든 유닛의 행동을 완료한 후 "턴 종료" 버튼을 클릭하여 적 턴으로 넘깁니다.

### 5.3 게임 승리 조건

- 모든 적 유닛을 제거하면 승리합니다.
- 모든 플레이어 유닛이 제거되면 패배합니다.

## 6. 개발자 정보

### 6.1 테스트 페이지

개발 및 테스트 목적으로 `/test` 경로에 테스트 페이지가 제공됩니다. 이 페이지에서는 다음과 같은 기능을 테스트할 수 있습니다:

- API 테스트: 새 게임 생성, 게임 상태 조회, 턴 종료 등
- 유닛 테스트: 유닛 이동, 유닛 공격 등
- UI 테스트: 메시지 표시, 모달 표시, 로딩 인디케이터 등

### 6.2 디버깅

로그 확인:
```bash
tail -f logs/spring-boot-logger.log
```

## 7. 커스터마이징

### 7.1 맵 크기 변경

`GameState.java` 파일에서 생성자의 매개변수를 수정하여 맵 크기를 변경할 수 있습니다:

```java
// GameService.java
public GameState createNewGame() {
    GameState gameState = new GameState(10, 10); // 가로, 세로 크기
    games.put(gameState.getId(), gameState);
    return gameState;
}
```

### 7.2 유닛 속성 변경

`GameState.java` 파일의 `initializeUnits()` 메서드에서 유닛 속성을 수정할 수 있습니다:

```java
Unit warrior = new Unit("전사", UnitType.WARRIOR, 1, 1);
warrior.setMaxHp(100);
warrior.setCurrentHp(100);
warrior.setAttackPower(20);
warrior.setMoveRange(3);
warrior.setAttackRange(1);
```

## 8. 문제 해결

### 8.1 일반적인 문제

- **서버가 시작되지 않는 경우**: 포트 8080이 이미 사용 중인지 확인하세요.
- **게임 로딩이 느린 경우**: 브라우저 캐시를 지우고 다시 시도하세요.
- **유닛이 이동하지 않는 경우**: 이동 범위 내의 타일인지, 다른 유닛이 이미 있는지 확인하세요.

### 8.2 포트 변경

`application.properties` 파일에서 서버 포트를 변경할 수 있습니다:

```properties
server.port=8081
```

## 9. 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.
