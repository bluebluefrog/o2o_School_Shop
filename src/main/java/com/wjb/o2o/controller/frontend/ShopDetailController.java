package com.wjb.o2o.controller.frontend;

import com.wjb.o2o.dto.ProductExecution;
import com.wjb.o2o.entity.Product;
import com.wjb.o2o.entity.ProductCategory;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.service.ProductCategoryService;
import com.wjb.o2o.service.ProductService;
import com.wjb.o2o.service.ShopService;
import com.wjb.o2o.utill.HttpServletRequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {

    @Resource
    private ShopService shopService;

    @Resource
    private ProductService productService;

    @Resource
    private ProductCategoryService productCategoryService;

    @GetMapping("/listshopdetailpageinfo")
    @ResponseBody
    private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long shopId = HttpServletRequestUtils.getLong(request, "shopId");
        Shop shop = null;
        List<ProductCategory> productCategoryList = null;
        if (shopId != -1) {
            shop = shopService.getByShopId(shopId);
            modelMap.put("shop", shop);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @GetMapping("/listproductsbyshop")
    @ResponseBody
    private Map<String, Object> listProductsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttpServletRequestUtils.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtils.getInt(request, "pageSize");
        Long shopId = HttpServletRequestUtils.getLong(request, "shopId");
        if ((pageIndex>-1) && (pageSize > -1) && (shopId > -1)) {
            Long productCategoryId = HttpServletRequestUtils.getLong(request, "productCategory");
            String productName = HttpServletRequestUtils.getString(request, "productName");
            Product productCondition = compactProductCondition4Search(shopId, productCategoryId, productName);
            ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
            modelMap.put("productList", pe.getProductList());
            modelMap.put("count", pe.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId != -1L) {
            // 查询某个商品类别下面的商品列表
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null) {
            // 查询名字里包含productName的店铺列表
            productCondition.setProductName(productName);
        }
        // 只允许选出状态为上架的商品
        productCondition.setEnableStatus(1);
        return productCondition;
    }
}
