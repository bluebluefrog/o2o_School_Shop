package com.wjb.o2o.service.impl;

import com.wjb.o2o.dto.ProductCategoryExecution;
import com.wjb.o2o.entity.ProductCategory;
import com.wjb.o2o.entity.ShopCategory;
import com.wjb.o2o.exceptions.ProductCategoryOperationException;
import com.wjb.o2o.mapper.ShopCategoryMapper;
import com.wjb.o2o.service.ShopCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Resource
    private ShopCategoryMapper shopCategoryMapper;

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategory) {
        return shopCategoryMapper.queryShopCategory(shopCategory);
    }
}