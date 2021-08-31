package com.wjb.o2o.service.impl;

import com.wjb.o2o.dto.ImageHolder;
import com.wjb.o2o.dto.ProductExecution;
import com.wjb.o2o.entity.Product;
import com.wjb.o2o.entity.ProductImg;
import com.wjb.o2o.enums.ProductStateEnum;
import com.wjb.o2o.exceptions.ProductCategoryOperationException;
import com.wjb.o2o.exceptions.ProductOperationException;
import com.wjb.o2o.mapper.ProductImgMapper;
import com.wjb.o2o.mapper.ProductMapper;
import com.wjb.o2o.service.ProductService;
import com.wjb.o2o.utill.ImageUtil;
import com.wjb.o2o.utill.PageCalculator;
import com.wjb.o2o.utill.PathUtil;
import javafx.scene.effect.Effect;
import org.omg.CORBA.portable.InputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductImgMapper productImgMapper;

    @Override
    @Transactional
    // 1.处理缩略图，获取缩略图相对路径并赋值给product
    // 2.往tb_product写入商品信息，获取productId
    // 3.结合productId批量处理商品详情图
    // 4.将商品详情图列表批量插入tb_product_img中
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgs) throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            // 给商品设置上默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            // 默认为上架的状态
            product.setEnableStatus(1);
            // 若商品缩略图不为空则添加

            if (thumbnail != null) {
                addThumbnail(product, thumbnail);
            }
            try {
                int effectNum = productMapper.insertProduct(product);
                if (effectNum <= 0) {
                    throw new ProductOperationException("创建商品失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品失败" + e.toString());
            }
            // 若商品详情图不为空则添加
            if (productImgs != null && productImgs.size() > 0) {
                addProductImgs(product,productImgs);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public Product getProductById(long productId) {
        return productMapper.queryProductById(productId);
    }

    @Override
    @Transactional
    // 1.若缩略图参数有值，则处理缩略图，
    // 若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
    // 2.若商品详情图列表参数有值，对商品详情图片列表进行同样的操作
    // 3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
    // 4.更新tb_product_img以及tb_product的信息
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgs) throws ProductCategoryOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            //给商品上默认属性
            product.setLastEditTime(new Date());
            // 若商品缩略图不为空且原有缩略图不为空则删除原有缩略图并添加
            if (thumbnail != null) {
                // 先获取一遍原有信息，因为原来的信息里有原图片地址
                Product tempProduct = productMapper.queryProductById(product.getProductId());
                if (tempProduct.getImgAddr() != null) {
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addThumbnail(product,thumbnail);
            }
            // 如果有新存入的商品详情图，则将原先的删除，并添加新的图片
            if (productImgs != null && productImgs.size() > 0) {
                deleteProductImgList(product.getProductId());
                addProductImgs(product,productImgs);
            }
            try{
                int effectNum = productMapper.updateProduct(product);
                if (effectNum <= 0) {
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            }catch(Exception e){
                throw new ProductOperationException("更新商品失败" + e.toString());
            }
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Product> productList = productMapper.queryProductList(productCondition, rowIndex, pageSize);
        int count = productMapper.queryProductCount(productCondition);
        ProductExecution pe = new ProductExecution();
        pe.setCount(count);
        pe.setProductList(productList);
        return pe;
    }

    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String shopImagePath = PathUtil.getShopImagePath(product.getShop().getShopId());
        String path = ImageUtil.generateThumbnail(thumbnail, shopImagePath);
        product.setImgAddr(path);
    }

    private void addProductImgs(Product product, List<ImageHolder> thumbnails) {
        String shopImagePath = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgs = new ArrayList<ProductImg>();
        for (ImageHolder productImg : thumbnails) {
            String path = ImageUtil.generateNorMalImg(productImg, shopImagePath);
            ProductImg img = new ProductImg();
            img.setImgAddr(path);
            img.setProductId(product.getProductId());
            img.setCreateTime(new Date());
            productImgs.add(img);
        }
        if (productImgs.size() > 0) {
            try {
                int effectNum = productImgMapper.batchInsertProductImg(productImgs);
                if (effectNum < 0) {
                    throw new ProductCategoryOperationException("创建商品详情失败");
                }
            } catch (Exception e) {
                throw new ProductCategoryOperationException("创建商品详情失败" + e.toString());
            }
        }
    }

    private void deleteProductImgList(long productId) {
        // 根据productId获取原来的图片
        List<ProductImg> productImgList = productImgMapper.queryProductImgList(productId);
        // 干掉原来的图片
        for (ProductImg productImg : productImgList) {
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        // 删除数据库里原有图片的信息
        productImgMapper.deleteProductImgByProductId(productId);
    }
}
