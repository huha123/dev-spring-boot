package dev.huha123.app.service;

import dev.huha123.app.dto.CategoryDto;
import dev.huha123.app.entity.CategoryEntity;
import dev.huha123.app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        CategoryEntity category = categoryDto.toEntity();
        return CategoryDto.fromEntity(categoryRepository.save(category));
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(String id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return CategoryDto.fromEntity(category);
    }

    @Transactional
    public CategoryDto updateCategory(String id, CategoryDto categoryDto) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category = category.toBuilder()
                .name(categoryDto.name())
                .isUse(categoryDto.isUse())
                .build();

        return CategoryDto.fromEntity(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }
}
