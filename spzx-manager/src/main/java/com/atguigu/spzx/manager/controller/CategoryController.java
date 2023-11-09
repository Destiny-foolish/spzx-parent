package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "根据parentId获取下级节点")
    @GetMapping(value = "/findCategoryList/{id}")
    public Result<List<Category>> findByParentId(@PathVariable(value = "id") Long parentId){
        List<Category> list = categoryService.findByParentId(parentId);
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "实现导出功能")
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response){
        categoryService.exportData(response);
    }

    //sping中使用MultipartFile可以识别多种文件
//    public Result importData(MultipartFile file) {
//        categoryService.importData(file);
//        return Result.build(null , ResultCodeEnum.SUCCESS) ;
//    }
    @Operation(summary = "实现导入功能")
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        categoryService.importData(file);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
}
