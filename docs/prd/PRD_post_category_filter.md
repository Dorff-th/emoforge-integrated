## 1. Feature Overview

Post List 화면 상단에 **카테고리 필터**를 표시하고  
사용자가 특정 카테고리를 선택하면 해당 카테고리의 Post만 조회하도록 한다.

현재는 모든 Post가 동일하게 표시되며 카테고리 기반 필터링 기능이 없다.

---

# 2. Goals

이 기능의 목적

- Post 목록을 카테고리 기준으로 탐색 가능하게 한다.
- 향후 카테고리 확장에 대비한 구조를 만든다.
- UI 구조를 크게 변경하지 않는다.
---

# 3. Scope

이번 작업에서 구현할 내용

1. Category API 호출  
2. Post List 상단에 Category Chip UI 노출  
3. Category 클릭 시 Post 목록 필터링

이번 작업에서 **제외**

- 태그 UI 노출  
- 태그 기반 검색  
- 태그 필터  
- Activity indicator

---

# 4. API Requirements

## Category List API

GET /api/categories

Response example

[  
  {  
    "id": 1,  
    "name": "Java"  
  },  
  {  
    "id": 2,  
    "name": "Spring Boot"  
  },  
  {  
    "id": 3,  
    "name": "Docker"  
  }  
]

---

## Post List API

기존 API를 활용하되 categoryId 파라미터를 추가한다.

GET /api/posts?categoryId={id}

또는

GET /api/posts?category={name}

---

# 5. UI Requirements

Post List 화면 상단에 Category Filter 영역을 추가한다.

Example UI

Search posts...  
  
[All] [Java] [Spring Boot] [Docker]

동작

All 클릭 → 전체 Post 조회  
  
Java 클릭 → Java category Post만 조회  
  
Spring Boot 클릭 → Spring Boot Post만 조회

---

# 6. UI Behavior

Category Chip 상태

Inactive

bg-gray-100  
text-gray-700

Active

bg-primary  
text-white

---

# 7. Data Flow

Page Load  
   ↓  
GET /api/categories  
   ↓  
Category chips render  
   ↓  
User clicks category  
   ↓  
Post list API 호출  
   ↓  
filtered posts render

---

# 8. Component Structure (Frontend)

예상 구조

features/post  
 ├ api  
 │   ├ postApi.ts  
 │   └ categoryApi.ts  
 │  
 ├ components  
 │   └ CategoryFilter.tsx  
 │  
 └ pages  
     └ PostListPage.tsx

---

# 9. Security Considerations

태그는 Post List 화면에 노출하지 않는다.

이유

- 악성 태그 입력 가능성
    
- UI 복잡도 증가
    

태그는 **Post Detail 화면에서만 노출한다.**

---

# 10. Acceptance Criteria

기능이 완료되면 다음이 가능해야 한다.

- Post List 상단에 Category 목록이 표시된다.
    
- Category 클릭 시 해당 카테고리 Post만 조회된다.
    
- All 클릭 시 전체 Post 목록이 표시된다.
    
- UI 레이아웃은 기존 구조를 유지한다.
    

---

# 11. Out of Scope (Future Work)

다음 단계에서 고려

- Category horizontal scroll  
- Tag UI  
- Activity indicator  
- Popular post indicator