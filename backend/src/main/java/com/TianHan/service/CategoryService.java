package com.TianHan.service;

import com.TianHan.mapper.CategoryMapper;
import com.TianHan.pojo.Category;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public List<Category> getAllCategories() {
        return categoryMapper.selectAll();
    }
} 