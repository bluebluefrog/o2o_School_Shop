package com.wjb.o2o.service.impl;

import com.wjb.o2o.dto.ProductCategoryExecution;
import com.wjb.o2o.entity.ProductCategory;
import com.wjb.o2o.enums.ProductCategoryStateEnum;
import com.wjb.o2o.exceptions.ProductCategoryOperationException;
import com.wjb.o2o.mapper.ProductCategoryMapper;
import com.wjb.o2o.mapper.ProductMapper;
import com.wjb.o2o.service.ProductCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Resource
    private ProductMapper productMapper;

    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return productCategoryMapper.queryProductCategoryList(shopId);
    }

    @Override
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        if (productCategoryList != null && productCategoryList.size() > 0) {
            try{
                int effectedNum = productCategoryMapper.batchInsertProductCategory(productCategoryList);
                if (effectedNum <= 0) {
                    throw new ProductCategoryOperationException("店铺类别创建失败");
                }else{
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            }catch (Exception e){
                throw new ProductCategoryOperationException("batchAddProductCategory error" + e.getMessage());
            }
        }else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) throws ProductCategoryOperationException {
        try {
            int effectNum = productMapper.updateProductCategoryToNull(productCategoryId);
            if (effectNum < 0) {
                throw new ProductCategoryOperationException("商品类别更新失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("deleteProductCategory error:" + e.getMessage());
        }
        try {
            int effectedNum = productCategoryMapper.deleteProductCategory(productCategoryId, shopId);
            if (effectedNum <= 0) {
                throw new ProductCategoryOperationException("商品类别删除失败");
            } else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("deleteProductCategory error:" + e.getMessage());
        }
    }
}
