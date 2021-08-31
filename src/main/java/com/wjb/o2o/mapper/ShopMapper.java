package com.wjb.o2o.mapper;

import com.wjb.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopMapper {
    //分页查询店铺，错参数需要加@Param来指定哪个参数是哪个
    /**
     * @param shopCondition
     * @param rowIndex 从第几行开始取数据
     * @param pageSize 返回的条数
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
                             @Param("rowIndex")int rowIndex, @Param("pageSize")int pageSize);

    int queryShopCount(@Param("shopCondition")Shop shopCondition);

    Shop queryByShopId(long shopId);

    int insertShop(Shop shop);

    int updateShop(Shop shop);
}
