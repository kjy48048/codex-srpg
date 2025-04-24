# 확장 기능 로드맵

본 문서는 프로젝트의 다음 단계 확장 기능을 범위별로 정리한 로드맵입니다.

## 1. 핵심 게임플레이 확장
- 카드 시스템
  - 덱/손패 UI, 코스트 시스템, 카드 효과 처리
- 상태 효과(Status Effects)
  - 중독, 기절, 버프/디버프, 지속 효과 관리
- 지형 및 장애물
  - 맵 타일별 이동·방어 보정, 불통행(Tile Blocking)
- 새로운 유닛 타입 및 특수 능력
  - 기병, 힐러, 다중공격, 스킬 시스템

## 2. 실시간 멀티플레이
- WebSocket을 이용한 실시간 게임 상태 동기화
- 방(Room) 또는 매치메이킹 로직
- 플레이어 식별 및 순차적 턴 제어

## 3. UI/UX 개선
- 애니메이션 및 이펙트 강화 (CSS, Canvas)
- 반응형 레이아웃 및 모바일/태블릿 지원
- 툴팁, 대화창(Dialogue), 모달 UX 개선
- 로딩 인디케이터 및 비동기 처리 피드백

## 4. 테스트 및 인프라
- E2E 테스트 (Cypress, Selenium)
- WebSocket 통합 테스트
- API 문서화(OpenAPI/Swagger)
- 컨테이너화(Docker, Docker Compose)
- CI/CD 파이프라인 (GitHub Actions)

## 5. 에디터 툴

### 5.1 Unit Editor
- CRUD REST API (`/api/editor/units`)
- `unit-data.yml` 기반 데이터 로드/저장
- `unit-editor.mustache` + JS UI

### 5.2 Map Editor
- CRUD REST API (`/api/editor/map`)
- `map-data.yml` 기반 맵 타일 편집
- 캔버스/그리드 UI

### 5.3 Scenario Editor
- 시나리오 데이터 모델 (`scenario-{id}.yml`)
  - 트리거, 이벤트 액션, 분기 선택지
- `ScenarioService` 및 `EventDispatcher`
- `scenario-editor.mustache` + 비주얼 에디터 UI
- 대화창(Dialogue box), 컷씬, 선택지 렌더링

### 5.4 다국어(i18n)
- 메시지 키(`unit.warrior.name`, `card.fireball.desc` 등) 사용
- `messages_{locale}.properties` 리소스 번들
- 서버(Mustache) 및 클라이언트(JS) 렌더링 연동

## 6. 문서화 및 개발 가이드
- README, CODE_STYLE.md 업데이트
- 코드 스타일 및 커밋 메시지 컨벤션
- pre-commit 훅 및 린터 설정

---
_위 로드맵을 기반으로 다음 사이클의 `TODO.md`를 작성하여 단계별 작업을 진행합니다._