package com.wjb.o2o.mapper;

import com.wjb.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCategoryMapper {
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategory);
}
