package com.wjb.o2o.mapper;

import com.wjb.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int insertProduct(Product product);

    Product queryProductById(long productId);

    int updateProduct(Product product);

    List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    int queryProductCount(@Param("productCondition") Product productCondition);

    int updateProductCategoryToNull(long productCategoryId);
}
