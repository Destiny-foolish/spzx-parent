package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectByParentId(Long parentId);

    int seleCount(Long id);

    List<Category> selectAll();

    //批量保存方法
    public abstract void batchInsert(List<Category> categoryList);



}
