package ru.practicum.category.mapper;


import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import ru.practicum.category.model.Category;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {
    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(null)
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .name(category.getName())
                .build();
    }

    public static List<Category> toCategoriesListDto(Page<Category> categories) {
        return categories.stream().collect(Collectors.toList());
    }
}
