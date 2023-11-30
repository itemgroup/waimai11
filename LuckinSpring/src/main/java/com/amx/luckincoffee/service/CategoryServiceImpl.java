package com.amx.luckincoffee.service;

import com.amx.luckincoffee.dao.CategoryDao;
import com.amx.luckincoffee.entity.Category;
import com.amx.luckincoffee.entity.Coffee;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Override
    public List<Category> queryAllCategory() {
        return categoryDao.queryAllCategory();
    }

    @Override
    public List<Coffee> queryCoffeeByCategory(int id) {
        return categoryDao.queryCoffeeByCategory(id);
    }
}
