# Codex Instructions for SRPG Web Game Project

## 🧩 Project Overview

This project is a 2D-style SRPG (Strategy Role-Playing Game) web application built with Spring Boot and Mustache.  
It features:
- Grid-based map system
- Turn-based combat
- Character stats & progression
- Frontend rendered via Mustache templates
- Real-time WebSocket interaction

## 📁 Tech Stack

- **Backend**: Spring Boot 2.7.x
- **Frontend**: HTML, CSS, JavaScript
- **Template Engine**: Mustache
- **Communication**: REST API, WebSocket
- **Build Tool**: Gradle (via Gradle Wrapper)

## 🎯 Codex Goals

When working in this repository, prioritize the following:

1. **Explain structure and flow**:
   - Break down the Spring Boot controller and service layers
   - Describe the game loop logic and WebSocket handling
2. **Add new gameplay features**:
   - Create new unit types with special abilities
   - Add status effects (e.g., poison, stun, buff)
   - Support map obstacles and terrain types
3. **Improve code quality**:
   - Refactor repeated code blocks
   - Add meaningful unit tests
   - Convert imperative logic into more modular components
4. **Support testing**:
   - Create mock data for new game states
   - Write endpoint tests using `@WebMvcTest`

## 🧪 Useful Commands

Run the Spring Boot server (Gradle):

```bash
./gradlew :srpg-game:bootRun
```

Build the project, skipping tests:

```bash
./gradlew build -x test
```

Run tests (requires JAVA_HOME or valid java on PATH):

```bash
./gradlew test
```

## 📝 Development Workflow

This project follows a task-driven workflow managed by the AI assistant:

1. The AI assistant outlines project goals and tasks in `TODO.md`.
2. The assistant implements each task, updates code, tests, and documentation.
3. Completed tasks are marked with [x] in `TODO.md`.
4. The user reviews changes, reports any issues, and the assistant fixes them before moving on.
5. The cycle repeats until all tasks are completed.

## 🗒️ 추가 지침
- 설명은 한글로 한다
