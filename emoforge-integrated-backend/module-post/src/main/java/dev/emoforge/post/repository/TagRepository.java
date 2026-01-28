package dev.emoforge.post.repository;


import dev.emoforge.post.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // name LIKE 'query%' 검색 (대소문자 무시)
    @Query("SELECT t.name FROM Tag t WHERE LOWER(t.name) LIKE LOWER(CONCAT(:prefix, '%')) ORDER BY t.name ASC")
    List<String> findByNameStartingWithIgnoreCase(@Param("prefix") String prefix);

    Optional<Tag> findByNameIgnoreCase(String name);
}
