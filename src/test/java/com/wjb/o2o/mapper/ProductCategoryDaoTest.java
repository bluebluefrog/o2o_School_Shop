package com.wjb.o2o.mapper;

import com.wjb.o2o.BaseTest;
import com.wjb.o2o.entity.ProductCategory;
import org.junit.Test;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ProductCategoryDaoTest extends BaseTest {

    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Test
    public void testQueryByShopId(){
        long shopId=29;
        List<ProductCategory> productCategoryList = productCategoryMapper.queryProductCategoryList(shopId);
        System.out.println(productCategoryList.size());
    }

    @Test
    public void testBatchInsertProductCategory() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryName("商品类别1");
        productCategory.setPriority(1);
        productCategory.setCreateTime(new Date());
        productCategory.setShopId(1L);
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setProductCategoryName("商品类别2");
        productCategory2.setPriority(2);
        productCategory2.setCreateTime(new Date());
        productCategory2.setShopId(1L);
        List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
        productCategoryList.add(productCategory);
        productCategoryList.add(productCategory2);
        int effectedNum = productCategoryMapper.batchInsertProductCategory(productCategoryList);
        assertEquals(2, effectedNum);
    }

    @Test
    public void testDeleteProductCategory() {
        long shopId=1;
        List<ProductCategory> productCategoryList = productCategoryMapper.queryProductCategoryList(shopId);
        for (ProductCategory pc:productCategoryList) {
            if ("商品类别1".equals(pc.getProductCategoryName()) || "商品类别2".equals(pc.getProductCategoryName())) {
                int effectNum = productCategoryMapper.deleteProductCategory(pc.getProductCategoryId(), shopId);
                assertEquals(1,effectNum);
            }
        }
    }
}
