package com.wjb.o2o.mapper;

import com.wjb.o2o.BaseTest;
import com.wjb.o2o.entity.ProductImg;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductImgDaoTest extends BaseTest {

    @Resource
    private ProductImgMapper productImgMapper;

    @Test
    public void insertImg(){
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("图片1");
        productImg1.setImgDesc("测试图片1");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(1L);
        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("图片2");
        productImg2.setPriority(1);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(1L);
        List productImgList=new ArrayList();
        productImgList.add(productImg1);
        productImgList.add(productImg2);
        int effectedNum = productImgMapper.batchInsertProductImg(productImgList);
        assertEquals(2, effectedNum);
    }

    @Test
    public void test2(){
        productImgMapper.deleteProductImgByProductId(1);
    }
}
