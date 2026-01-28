package dev.emoforge.post.admin.service;

import dev.emoforge.post.domain.Category;
import dev.emoforge.post.repository.CategoryRepository;
import dev.emoforge.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  관리자 화면에서 카테고리를 추가하거나 삭제한다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private  final PostRepository postRepository;
    //미분류 카테고리는 절대 삭제할 수 없기에 관리자화면에서도 안보이도록 조회
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        // 기본 카테고리 제외
        return categoryRepository.findAllByDefaultCategoryFalse();
    }

    //관리자 화면에서 카테고리 추가
    @Transactional
    public Category createCategory(String name) {
        //카테고리 추가할때 동일한 카테고리가 있는제 체크
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다: " + name);
        }

        Category category = new Category();
        category.setName(name);
        category.setDefaultCategory(false);
        return categoryRepository.save(category);
    }

    //관리자 페이지에서 카테고리 이름 수정
    @Transactional
    public Category updateCategory(Long categoryId, String newName) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));

        if (category.isDefaultCategory()) {
            throw new IllegalStateException("기본 카테고리는 수정할 수 없습니다.");
        }

        if (categoryRepository.existsByName(newName)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다: " + newName);
        }

        category.setName(newName);
        return categoryRepository.save(category);
    }

    //관리자 페이지에서 카테코리 삭제
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));

        if (category.isDefaultCategory()) {
            throw new IllegalStateException("기본 카테고리는 삭제할 수 없습니다.");
        }
        // 1. 기본 카테고리 조회
        Category defaultCategory = categoryRepository.findByDefaultCategoryTrue()
            .orElseThrow(() -> new IllegalStateException("기본 카테고리가 존재하지 않습니다."));

        log.debug(" ****** categoryId : " + categoryId + " , defaultCategory.getId() : " + defaultCategory.getId());

        // 2. Post 카테고리 변경
        int updatedCnt = postRepository.movePostsToDefaultCategory(defaultCategory.getId(), categoryId);
        log.debug("*************updatedCnt : " + updatedCnt);

        categoryRepository.delete(category);
    }

}
