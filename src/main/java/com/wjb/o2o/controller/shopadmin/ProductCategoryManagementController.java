package com.wjb.o2o.controller.shopadmin;

import com.wjb.o2o.dto.ProductCategoryExecution;
import com.wjb.o2o.dto.Result;
import com.wjb.o2o.entity.ProductCategory;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.enums.ProductCategoryStateEnum;
import com.wjb.o2o.exceptions.ProductCategoryOperationException;
import com.wjb.o2o.service.ProductCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {

    @Resource
    private ProductCategoryService productCategoryService;

    @GetMapping("/getproductcategorylist")
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {

        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> list = null;
        if (currentShop != null && currentShop.getShopId() > 0) {
            list = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true, list);
        } else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
        }
    }

    @PostMapping("/addproductcategorys")
    @ResponseBody
    private Map<String,Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Shop shop = (Shop)request.getSession().getAttribute("currentShop");
        for (ProductCategory pc:productCategoryList){
            pc.setShopId(shop.getShopId());
        }
        if (productCategoryList != null && productCategoryList.size() > 0) {
            try{
                ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
                if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            }catch (ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg", "请至少输入一个商品类别");
        }
        return modelMap;
    }

    @PostMapping("/removeproductcategory")
    @ResponseBody
    public Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (productCategoryId != null && productCategoryId > 0) {
        try{
            Shop shop = (Shop)request.getSession().getAttribute("currentShop");
            ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId, shop.getShopId());
            if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            }catch (ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg", "请至少输入一个商品类别");
        }
        return modelMap;
    }
}
