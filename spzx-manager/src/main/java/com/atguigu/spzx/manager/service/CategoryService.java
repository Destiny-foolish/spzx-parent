package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.entity.product.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findByParentId(Long parentId);
}
