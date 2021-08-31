package com.wjb.o2o.mapper;

import com.wjb.o2o.entity.Product;
import com.wjb.o2o.entity.ProductImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductImgMapper {

    int batchInsertProductImg(List<ProductImg> productImgList);

    int deleteProductImgByProductId(long productId);

    List<ProductImg> queryProductImgList(long productId);


}
