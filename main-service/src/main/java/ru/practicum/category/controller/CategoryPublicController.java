package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> findAllCategory(@RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                          @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return categoryService.findAllCategory(from, size);
    }

    @GetMapping("/{catId}")
    public Category getCategoryById(@PathVariable Long catId) {
        return categoryService.getById(catId);
    }
}
