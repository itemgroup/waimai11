package com.amx.luckincoffee.controller;

import com.amx.luckincoffee.entity.Category;
import com.amx.luckincoffee.entity.Coffee;
import com.amx.luckincoffee.entity.Result;
import com.amx.luckincoffee.service.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/luckin")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/category")
    public Result getCategory(){
        Result result = new Result();

        List<Category> categories = categoryService.queryAllCategory();
        if (categories.size() > 0){
            result.setCode(200);
            result.setMessage("获取分类数据成功");
            result.setData(categories);
        }else {
            result.setCode(500);
            result.setMessage("获取分类数据失败");
        }
        return result;
    }

    @GetMapping("/coffee")
    public Result getCoffeeByCategory(@RequestParam("id")int id){
        Result result = new Result();

        List<Coffee> coffees = categoryService.queryCoffeeByCategory(id);
        if (coffees.size() > 0){
            result.setCode(200);
            result.setMessage("获取分类数据成功");
            result.setData(coffees);
        }else {
            result.setCode(500);
            result.setMessage("获取分类数据失败");
        }
        return result;
    }
}
