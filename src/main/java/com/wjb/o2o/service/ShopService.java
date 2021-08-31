package com.wjb.o2o.service;

import com.wjb.o2o.dto.ImageHolder;
import com.wjb.o2o.dto.ShopExecution;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.exceptions.ShopOperationException;

import java.io.InputStream;

public interface ShopService {

    ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);

    Shop getByShopId(long shopId)throws ShopOperationException;

    ShopExecution modifyShop(Shop shop, ImageHolder thumbnail)throws ShopOperationException;

    ShopExecution addShop(Shop shop,ImageHolder thumbnail)throws ShopOperationException;
}
