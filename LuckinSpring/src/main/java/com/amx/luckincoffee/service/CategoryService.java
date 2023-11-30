package com.amx.luckincoffee.service;

import com.amx.luckincoffee.entity.Category;
import com.amx.luckincoffee.entity.Coffee;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryService {
    List<Category> queryAllCategory();

    List<Coffee> queryCoffeeByCategory(int id);
}
