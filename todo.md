# TODO

이 문서에는 프로젝트의 주요 작업 항목(TODO)을 정리합니다.

## 1. 환경 설정 및 초기화
- [x] `GameDataProperties` YAML 바인딩 오류 수정 (prefix 설정 및 EnableConfigurationProperties 적용)
  - `@ConfigurationProperties(prefix="game")` 추가
  - `@EnableConfigurationProperties(GameDataProperties.class)` 설정 삽입
  - `game-data.yml` 필드 이름을 프로퍼티 바인딩 규칙에 맞게 수정
- [x] `GameService#createNewGame`에 `GameStateFactory` 적용 (GameStateFactory 주입 및 사용)
  - 하드코딩된 `new GameState(10, 10)` 대신 팩토리 메서드 사용
  - `GameState` 생성자 내 고정 초기화 로직 제거

## 2. 카드 템플릿 로딩
- [x] 카드 템플릿용 디렉토리 및 샘플 YAML 추가 (srpg-game/src/main/resources/game-data/templates/*)
  - `srpg-game/src/main/resources/game-data/templates/*.yml`
- [x] `CardTemplateLoader` 리소스 경로 및 로딩 로직 검증 (빈 템플릿 처리 로직 추가)
- [x] `GameState.initializeCardSystem`에 템플릿 로더 통합 (카드 덱 초기화 시 로더 사용)

## 3. GameState 리팩토링
- [x] 생성자에서 초기 유닛 배치 로직 제거
- [x] 맵 초기화와 유닛 초기화 책임 분리
- [x] `GameStateFactory` 사용하도록 서비스 로직 변경

## 4. 테스트 추가
- [x] `GameStateFactory` 단위 테스트 작성
- [x] `GameService` 이동/공격/턴 처리 로직 테스트
- [x] REST API 및 WebSocket 컨트롤러 테스트 (`@WebMvcTest`, `@SpringBootTest`)

## 5. 프론트엔드 개선
- [x] 동적 맵 크기 지원 (10×10 하드코딩 제거)
- [x] 서버-클라이언트 간 이동/공격 범위 계산 일관성 검증
- [x] `game.js` 모듈화 및 리팩토링

## 6. 문서화 및 기타
- [ ] README 업데이트 (빌드/실행 가이드, 아키텍처 개요)
- [ ] 개발 가이드 및 코드 스타일 문서화