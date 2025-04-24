# TODO: 카드 에디터 및 데이터 모델 설계 TODO

## 1. 카드 데이터 모델 설계
- [ ] `card-data.yml` 포맷 정의 (필드: id, nameKey, type, cost, descriptionKey, effectType, effectParams) 및 샘플 카드 3종 작성
- [ ] CardDataProperties 클래스 설계 (Spring @ConfigurationProperties) 및 YAML 매핑 확인
- [ ] GameState.initializeCardSystem 로직 검토 및 카드 시스템 확장 포인트 문서화

## 2. 다국어 메시지 키 통합
- [ ] `nameKey`, `descriptionKey` 기본 키 네이밍 컨벤션 및 `messages_{locale}.properties` 예시 작성 (en, ko)
- [ ] Mustache 템플릿에서 메시지 키 치환 방법 문서화 (`{{msg key}}`)

## 3. 카드 에디터 UI 설계 (개요)
- [ ] `card-editor.mustache` 레이아웃 초안 설계 (카드 목록 테이블, 신규/수정 Modal)
  - 테이블 Columns: id, nameKey, type, cost, actions
  - Modal Form Fields: nameKey, type(select), cost, descriptionKey(textarea), effect 설정
- [ ] card-editor.js 기능 흐름 정의 (데이터 로드, 이벤트 핸들러, AJAX 요청, 유효성 검사)

## 4. API 엔드포인트 설계
- [ ] GET `/api/editor/cards` → 카드 목록 JSON 반환 설계
- [ ] POST `/api/editor/cards` → 카드 생성 및 수정 API 설계
- [ ] DELETE `/api/editor/cards/{id}` → 카드 삭제 API 설계
- [ ] 위 API 대한 단위 테스트 (`@WebMvcTest`, MockMvc) 및 테스트 케이스 목록 작성