package com.wjb.o2o.mapper;

import com.wjb.o2o.BaseTest;
import com.wjb.o2o.entity.Area;
import com.wjb.o2o.entity.PersonInfo;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest {
    @Autowired
    private ShopMapper shopMapper;

    @Test
    public void testQueryShopList(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        shopCondition.setOwner(owner);
        shopCondition.setArea(area);
        List<Shop> shops = shopMapper.queryShopList(shopCondition, 0, 5);
        int count = shopMapper.queryShopCount(shopCondition);

        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(3L);
        shopCondition.setShopCategory(sc);
        List<Shop> shops1 = shopMapper.queryShopList(shopCondition, 0, 2);
        int count1 = shopMapper.queryShopCount(shopCondition);


        System.out.println("店铺列表的大小:"+shops.size());
        System.out.println(count);
        System.out.println("店铺列表的大小:"+shops1.size());
        System.out.println(count1);

        for (Shop s:shops) {
            System.out.println(s.getShopId());
            System.out.println(s.getArea());
        }
    }

    @Test
    public void testQueryByShopId() {
        long shopId = 1l;
        Shop shop = shopMapper.queryByShopId(shopId);
        System.out.println("areaId:" + shop.getArea().getAreaId());
        System.out.println("areaName" + shop.getArea().getAreaName());
    }

    @Test
    public void testInsertShop() {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(10L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试的店铺");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(0);
        shop.setAdvice("审核中");
        int effectedNum = shopMapper.insertShop(shop);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testUpdateShop() {
        Shop shop = new Shop();
        shop.setShopId(52L);
        shop.setShopAddr("new111Test");
        int effectedNum = shopMapper.updateShop(shop);
        assertEquals(1, effectedNum);
    }
}
