package com.TianHan.controller;

import com.TianHan.pojo.Category;
import com.TianHan.service.CategoryService;
import com.TianHan.utils.AuthAccess;
import com.TianHan.utils.Result;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping
    @AuthAccess
    public Result getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }
} 