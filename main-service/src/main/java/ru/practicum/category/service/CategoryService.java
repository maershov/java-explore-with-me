package ru.practicum.category.service;

import ru.practicum.category.model.Category;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    Category createNewCategory(CategoryDto categoryDto);

    void removeCategory(Long id);

    Category updateCategory(Long id, CategoryDto categoryDto);

    List<Category> findAllCategory(int from, int size);

    Category getById(Long id);
}
