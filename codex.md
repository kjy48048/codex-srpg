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
- **Build Tool**: Maven

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

Run the Spring Boot server:

```bash
mvn spring-boot:run
