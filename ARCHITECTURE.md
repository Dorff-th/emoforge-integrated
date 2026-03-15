# ARCHITECTURE.md

System architecture documentation for the Emoforge project.

This document explains the overall system structure,
component relationships, and data flow.

---

# 1. System Overview

Emoforge is a personal web service platform consisting of:

- Post board system
- Emotion diary system
- AI feedback service
- Admin management

The system uses a **Monolithic backend architecture**
with a **separate AI service**.

---

# 2. High-Level Architecture

Main system components:

User Browser
    │
    ▼
Nginx (Reverse Proxy)
    │
    ▼
Frontend (React + Vite)
    │
    ▼
Backend API (Spring Boot)
    │
    ├── Database (MySQL RDS)
    │
    └── AI Service (FastAPI)

---

# 3. Technology Stack

Backend
- Java 17
- Spring Boot 3
- Spring Security
- JWT Authentication
- JPA + MyBatis

Frontend
- React
- Vite
- TailwindCSS

Database
- Development: MariaDB
- Production: MySQL (AWS RDS)

Infrastructure
- Docker
- Nginx
- AWS EC2
- GitHub Actions

AI Service
- Python
- FastAPI
- OpenAI API

---

# 4. Backend Architecture

The backend follows a layered architecture.

Controller
Handles HTTP requests and responses.

Service
Contains business logic.

Repository
Handles database access using JPA or MyBatis.

Example flow:

Client Request
→ Controller
→ Service
→ Repository
→ Database

---

# 5. Frontend Architecture

The frontend uses a **feature-based folder structure**.

frontend/

features/
    post/
    diary/
    admin/

shared/
    components/
    utils/

Guidelines:

- Each feature manages its own UI and logic.
- Shared utilities and components are placed in shared/.

---

# 6. Database Architecture

Development Environment
MariaDB

Production Environment
MySQL (AWS RDS)

Guidelines:

- SQL queries must be compatible with MariaDB and MySQL.
- JPA is used for standard CRUD operations.
- MyBatis is used for complex queries and search.

---

# 7. AI Service Architecture

AI features are handled by a separate FastAPI service.

Reason for separation:

- Better performance for LLM calls
- Reduced load on Spring Boot backend
- Independent scaling possible

Request flow:

Frontend
→ Backend API
→ AI Service
→ OpenAI API

---

# 8. Deployment Architecture

Production deployment runs on AWS EC2.

Main components:

- Nginx
- Frontend container
- Backend container
- AI service container

Database runs on AWS RDS.

---

# 9. CI/CD Pipeline

Deployment uses GitHub Actions.

Pipeline flow:

Developer push
→ GitHub Actions
→ Docker image build
→ Image push
→ Server deploy

---

# 10. Design Principles

The system follows these principles:

- Keep architecture simple
- Prefer consistency over complexity
- Minimize infrastructure overhead
- Separate AI workload from backend API

---

# End of ARCHITECTURE.md