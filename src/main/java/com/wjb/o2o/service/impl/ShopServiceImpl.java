package com.wjb.o2o.service.impl;

import com.wjb.o2o.dto.ImageHolder;
import com.wjb.o2o.mapper.ShopMapper;
import com.wjb.o2o.dto.ShopExecution;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.enums.ShopStateEnum;
import com.wjb.o2o.exceptions.ShopOperationException;
import com.wjb.o2o.service.ShopService;
import com.wjb.o2o.utill.ImageUtil;
import com.wjb.o2o.utill.PageCalculator;
import com.wjb.o2o.utill.PathUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Resource
    ShopMapper shopDao;

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Shop> shops = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se=new ShopExecution();
        if (shops != null) {
            se.setShopList(shops);
            se.setCount(count);
        }
        else{
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    @Override
    public Shop getByShopId(long shopId) throws ShopOperationException {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            //1判断是否需要图片
            try{
            if (thumbnail.getImage()!= null && thumbnail.getImageName() != null && !thumbnail.getImageName().equals("")) {//判断图片不为空
                Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                if (tempShop.getShopImg() != null) {
                    ImageUtil.deleteFileOrPath(tempShop.getShopImg());//存在图片则删除
                }
                addShopImg(shop,thumbnail);//不存在则添加
            }
            //2更新店铺信息
            shop.setLastEditTime(new Date());
            int effectedNum = shopDao.updateShop(shop);
            if (effectedNum <= 0) {
                return new ShopExecution(ShopStateEnum.INNER_ERROR);
            } else {
                shop = shopDao.queryByShopId(shop.getShopId());
                return new ShopExecution(ShopStateEnum.SUCCESS,shop);
            }}catch (Exception e){
                throw new ShopOperationException("modifyShop error:" + e.getMessage());//以上任何一个步骤出错都抛出异常
            }
        }
    }

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop,ImageHolder thumbnail) {
        //空值判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }try{
            //给店铺信息赋值，初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0) {
                throw new ShopOperationException("店铺创建失败");
            }else{
                if (thumbnail.getImage() != null) {
                    //存储图片
                    try {
                        addShopImg(shop, thumbnail);
                    }catch(Exception e){
                        throw new ShopOperationException("add ShopImg error"+e.getMessage());
                    }
                    //更新店铺的图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        }catch(Exception e){
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    private void addShopImg(Shop shop,ImageHolder thumbnail) {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        shop.setShopImg(shopImgAddr);
    }
}
