package com.wjb.o2o.service;

import com.wjb.o2o.BaseTest;
import com.wjb.o2o.dto.ShopExecution;
import com.wjb.o2o.entity.Area;
import com.wjb.o2o.entity.PersonInfo;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.entity.ShopCategory;
import com.wjb.o2o.enums.ShopStateEnum;
import com.wjb.o2o.exceptions.ShopOperationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void testGetShopList(){
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(10L);
        shopCondition.setShopCategory(sc);
        ShopExecution se = shopService.getShopList(shopCondition, 1, 2);
        System.out.println(se.getShopList().size());
        System.out.println(se.getCount());
    }

    @Test

    public void testModifyShop() throws ShopOperationException, FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("修改后的店铺名称");
        File shopImg = new File("/Users/handsomewarren/Desktop/商铺/酸奶牛/WechatIMG31.jpeg");
        InputStream is = new FileInputStream(shopImg);
        //ShopExecution shopExecution = shopService.modifyShop(shop, is,"WechatIMG31.jpg");
       // System.out.println("新的图片地址为：" + shopExecution.getShop().getShopImg());
    }

    @Test
    public void testAddShop() throws FileNotFoundException {
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
        shop.setShopName("测试的店铺2");
        shop.setShopDesc("test1");
        shop.setShopAddr("test1");
        shop.setPhone("test1");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
        File shopImg = new File("C:\\Users\\user\\Pictures\\壁纸\\jellybear.jpg");
        InputStream is = new FileInputStream(shopImg);
        //ShopExecution se = shopService.addShop(shop, is,shopImg.getName());
        //assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }
}
