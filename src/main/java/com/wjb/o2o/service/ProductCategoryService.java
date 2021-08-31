package com.wjb.o2o.service;

import com.wjb.o2o.dto.ProductCategoryExecution;
import com.wjb.o2o.entity.ProductCategory;
import com.wjb.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getProductCategoryList(Long shopId);

    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
            throws ProductCategoryOperationException;

    ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId)
        throws ProductCategoryOperationException;
}
