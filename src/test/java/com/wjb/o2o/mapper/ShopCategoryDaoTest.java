package com.wjb.o2o.mapper;

import com.wjb.o2o.BaseTest;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.entity.ShopCategory;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopCategoryDaoTest extends BaseTest {

    @Resource
    ShopCategoryMapper shopCategoryMapper;

    @Resource
    ShopMapper shopMapper;

    @Test
    public void testQureyShopCategory() {
        List<ShopCategory> shopCategories = shopCategoryMapper.queryShopCategory(null);
        assertEquals(6, shopCategories.size());
    }

    @Test
    public void testQueryShopListAndCount() {
        Shop shopCondition = new Shop();
        ShopCategory childCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();
        parentCategory.setShopCategoryId(12L);
        childCategory.setParent(parentCategory);
        shopCondition.setShopCategory(childCategory);
        List<Shop> shopList = shopMapper.queryShopList(shopCondition, 0, 6);
        int count = shopMapper.queryShopCount(shopCondition);
        System.out.println("店铺列表的大小：" + shopList.size());
        System.out.println("店铺总数：" + count);
    }
}
