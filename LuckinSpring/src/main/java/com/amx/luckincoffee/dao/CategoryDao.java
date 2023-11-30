package com.amx.luckincoffee.dao;

import com.amx.luckincoffee.entity.Category;
import com.amx.luckincoffee.entity.Coffee;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface CategoryDao {
    List<Category> queryAllCategory();

    List<Coffee> queryCoffeeByCategory(@RequestParam("id") int id);


}
