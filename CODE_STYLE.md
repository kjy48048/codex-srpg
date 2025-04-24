# 개발 가이드 및 코드 스타일

이 문서는 프로젝트 전반에 걸친 코드 작성 및 커밋 컨벤션을 안내합니다.

## 1. 프로젝트 구조
- `app/`: Codex CLI 예제 애플리케이션
- `srpg-game/`: Spring Boot SRPG 게임 모듈
  - `src/main/java`: 도메인, 서비스, 컨트롤러, 설정
  - `src/main/resources`: 정적 리소스(css, js), 템플릿, game-data.yml
  - `src/test/java`: 단위 테스트, MVC 테스트

## 2. Java 코드 스타일
- Indent: 4 spaces, no tabs
- 클래스명: PascalCase, 예: `GameService`, `WebSocketController`
- 메소드명/변수명: lowerCamelCase, 예: `createNewGame()`, `gameState`
- 상수: UPPER_SNAKE_CASE, 예: `MAX_HP`
- 필드 주입 대신 생성자 주입 사용
- Lombok 사용: `@Getter`, `@Setter`, `@RequiredArgsConstructor` 등 활용

## 3. 테스트 작성 기준
- 단위 테스트: JUnit Jupiter 사용, 메소드명에 `Test` 접미사
- MockMvc: `@WebMvcTest` 또는 `@SpringBootTest`로 컨트롤러 테스트
- Mockito: 의존성 주입을 모킹, `when`/`verify`로 상호작용 검증

## 4. 커밋 메시지 컨벤션
- feat: 새로운 기능 추가
- fix: 버그 수정
- refactor: 코드 리팩토링
- test: 테스트 코드 추가/수정
- docs: 문서 변경
- chore: 빌드/스크립트/설정 변경

예시: `feat: game.js 모듈화 및 리팩토링`

## 5. JavaScript 코드 스타일
- Indent: 2 spaces
- 변수 선언: `const`, `let` 사용, `var` 사용 지양
- 모듈 패턴: 전역 네임스페이스 사용 최소화, `GameApp` 객체로 기능 캡슐화
- 이벤트 핸들러 등록 후 반드시 해제 고려
- DOM 조작: 가급적 템플릿이나 데이터 바인딩 라이브러리 사용 권장

## 6. Pre-commit Hook
- `.pre-commit-config.yaml`을 설정하여 포맷터, 린터를 실행할 수 있습니다.

```bash
pre-commit run --all-files
```