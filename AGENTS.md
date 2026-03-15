# AGENTS.md

This document provides guidelines for AI agents working on this project.

---

# Project Overview

Project: Emoforge

Purpose:
A personal platform for diary, posts, and AI-assisted feedback.

Architecture:
Monolith backend + React frontend.

---

# Tech Stack

Backend
- Spring Boot 3
- JPA + MyBatis
- Java 17

Frontend
- React
- TailwindCSS
- Axios

Database
- Development: MariaDB
- Production: MySQL (AWS RDS)

---

# Project Structure

backend/
frontend/

docs/
  ideas/
  prd/
  history/
  prompts/

---

# Coding Rules

Follow existing coding conventions.

Do not:

- Modify unrelated files
- Change database schema unless explicitly requested
- Modify authentication logic without instruction
- Change API contracts without instruction

---

# Feature Implementation Workflow

When implementing a feature:

1. Read AGENTS.md
2. Read ARCHITECTURE.md
3. Read the relevant PRD in docs/prd/

Example:

docs/prd/PRD-post-list-ui.md

4. Generate implementation steps if necessary
5. Implement the feature
6. Ensure existing functionality is not broken
7. Write a summary to docs/history/

---

# History Rules

After completing a feature:

Create a history entry:

docs/history/YYYY-MM-DD-feature-name.md

Include:

- Feature name
- Files modified
- Summary of changes

---

# PRD Rules

Each feature should have its own PRD file.

Location:

docs/prd/

Example:

PRD-post-list-ui.md

PRD should contain:

- Goal
- Scope
- Requirements
- Constraints
- Acceptance Criteria

---

# AI Agent Behavior

Agents should:

- Focus only on the requested feature
- Avoid modifying unrelated modules
- Follow the project architecture

---
# Task Execution Rules

Before implementing a feature:

1. Read the relevant PRD
2. Define a short task plan
3. Implement the task incrementally
4. Avoid unnecessary refactoring
5. Ensure no infinite loops or repeated API calls
6. Write a history report

Task plan should include:

- Goal
- Files to modify
- Implementation steps