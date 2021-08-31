package com.wjb.o2o.service;

import com.wjb.o2o.dto.ImageHolder;
import com.wjb.o2o.dto.ProductExecution;
import com.wjb.o2o.entity.Product;
import com.wjb.o2o.exceptions.ProductCategoryOperationException;
import com.wjb.o2o.exceptions.ProductOperationException;
import org.omg.CORBA.portable.InputStream;

import java.util.List;

public interface ProductService {
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgs)throws ProductOperationException;

    Product getProductById(long productId);

    ProductExecution modifyProduct(Product product, ImageHolder thumbnail,List<ImageHolder>productImgs)throws ProductCategoryOperationException;

    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

}
