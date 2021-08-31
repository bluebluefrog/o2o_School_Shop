package com.wjb.o2o.controller.frontend;

import com.wjb.o2o.dto.ShopExecution;
import com.wjb.o2o.entity.Area;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.entity.ShopCategory;
import com.wjb.o2o.service.AreaService;
import com.wjb.o2o.service.ShopCategoryService;
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
public class ShopListController {

    @Resource
    private AreaService areaService;

    @Resource
    private ShopCategoryService shopCategoryService;

    @Resource
    private ShopService shopService;

    @GetMapping("/listshopspageinfo")
    @ResponseBody
    private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long parentId = HttpServletRequestUtils.getLong(request, "parentId");
        List<ShopCategory> shopCategoryList = null;
        if (parentId != -1) {
            try{
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            }catch(Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }else{
            try{
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            }catch(Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        modelMap.put("shopCategoryList", shopCategoryList);
        List<Area> areaList = null;
        try{
            areaList = areaService.getAreaList();
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
            return modelMap;
        }catch(Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @GetMapping("listshops")
    @ResponseBody
    private Map<String, Object> listShops(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttpServletRequestUtils.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtils.getInt(request, "pageSize");
        if ((pageIndex > -1) && (pageSize > -1)) {
            Long parentId = HttpServletRequestUtils.getLong(request, "parentId");
            Long shopCategoryId = HttpServletRequestUtils.getLong(request, "shopCategoryId");
            int areaId = HttpServletRequestUtils.getInt(request, "areaId");
            String shopName = HttpServletRequestUtils.getString(request, "shopName");
            Shop shopCondition=compactShopConditionForSearch(parentId, shopCategoryId, areaId, shopName);
            ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
            modelMap.put("shopList", se.getShopList());
            modelMap.put("count", se.getCount());
            modelMap.put("success", true);
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex");
        }
        return modelMap;
    }

    private Shop compactShopConditionForSearch(Long parentId, Long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId != -1L) {
            // 查询某个一级ShopCategory下面的所有二级ShopCategory里面的店铺列表
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if (shopCategoryId != -1L) {
            // 查询某个二级ShopCategory下面的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId != -1L) {
            // 查询位于某个区域Id下的店铺列表
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }

        if (shopName != null) {
            // 查询名字里包含shopName的店铺列表
            shopCondition.setShopName(shopName);
        }
        // 前端展示的店铺都是审核成功的店铺
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
