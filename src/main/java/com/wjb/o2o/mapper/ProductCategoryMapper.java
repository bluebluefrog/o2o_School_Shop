package com.wjb.o2o.mapper;

import com.wjb.o2o.dto.ProductCategoryExecution;
import com.wjb.o2o.entity.ProductCategory;
import com.wjb.o2o.exceptions.ProductCategoryOperationException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryMapper {
    List<ProductCategory> queryProductCategoryList(long shopId);

    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    int deleteProductCategory(@Param("productCategoryId") long productCategory,@Param("shopId") long shopId);
}
