package dev.emoforge.diary.repository;


import dev.emoforge.diary.domain.DiaryEntry;
import dev.emoforge.diary.dto.response.DiarySearchResultDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DiaryEntrySearchRepositoryImpl implements DiaryEntrySearchRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<DiarySearchResultDTO> searchDiaries(
            String query,
            List<String> fields,
            String memberUuid,
            Map<String, Integer> emotionMap,
            Map<String, LocalDate> dateMap,
            Pageable pageable) {

        // 검색어가 null이면 빈 문자열로 처리
        query = (query == null) ? "" : query.toLowerCase();

        // 결과 쿼리
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DiaryEntry> cq = cb.createQuery(DiaryEntry.class);
        Root<DiaryEntry> root = cq.from(DiaryEntry.class);

        List<Predicate> predicates = buildSearchPredicates(cb, root, query, fields, memberUuid, emotionMap, dateMap);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.desc(root.get("createdAt")));

        TypedQuery<DiaryEntry> queryResult = em.createQuery(cq);
        queryResult.setFirstResult((int) pageable.getOffset());
        queryResult.setMaxResults(pageable.getPageSize());
        List<DiaryEntry> resultList = queryResult.getResultList();

        // 카운트 쿼리 (root 새로 생성)
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<DiaryEntry> countRoot = countQuery.from(DiaryEntry.class);

        List<Predicate> countPredicates = buildSearchPredicates(cb, countRoot, query, fields, memberUuid, emotionMap, dateMap);
        countQuery.select(cb.count(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long totalCount = em.createQuery(countQuery).getSingleResult();

        // DTO 변환
        List<DiarySearchResultDTO> dtoList = resultList.stream()
                .map(entry -> DiarySearchResultDTO.builder()
                        .id(entry.getId())
                        .diaryDate(entry.getDiaryDate())
                        .content(entry.getContent())
                        .feelingKo(entry.getFeelingKo())
                        .feelingEn(entry.getFeelingEn())
                        .feedback(entry.getFeedback())
                        .emotion(entry.getEmotion())
                        .createdAt(entry.getCreatedAt())
                        .build())
                .toList();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }


    private List<Predicate> buildSearchPredicates(
            CriteriaBuilder cb,
            Root<DiaryEntry> root,
            String query,
            List<String> fields,
            String memberUuid,
            Map<String, Integer> emotionMap,
            Map<String, LocalDate> dateMap
    ) {
        List<Predicate> predicates = new ArrayList<>();

        // 로그인된 사용자 조건
        //predicates.add(cb.equal(root.get("member").get("id"), memberUuid));
        predicates.add(cb.equal(root.get("memberUuid"), memberUuid));

        // 감정 점수 조건
        if (emotionMap != null && !emotionMap.isEmpty()) {
            predicates.add(cb.between(
                    root.get("emotion"),
                    emotionMap.get("min"),
                    emotionMap.get("max")
            ));
        }

        // 날짜 범위 조건
        if (dateMap != null && !dateMap.isEmpty()) {
            predicates.add(cb.between(
                    root.get("diaryDate"),
                    dateMap.get("startDate"),
                    dateMap.get("endDate")
            ));
        }

        // 텍스트 검색 조건
        if (query != null && !query.isBlank()) {
            String keyword = "%" + query + "%";
            List<Predicate> orList = new ArrayList<>();
            List<String> targetFields = (fields == null || fields.isEmpty())
                    ? List.of("feelingKo", "feelingEn", "content", "feedback")
                    : fields;

            for (String field : targetFields) {
                Expression<String> fieldExpr = cb.lower(root.get(field).as(String.class));
                orList.add(cb.like(fieldExpr, cb.literal(keyword)));
            }
            if (!orList.isEmpty()) {
                predicates.add(cb.or(orList.toArray(new Predicate[0])));
            }
        }

        return predicates;
    }


}
