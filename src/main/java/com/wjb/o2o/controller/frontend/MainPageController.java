package com.wjb.o2o.controller.frontend;

import com.sun.javafx.collections.ImmutableObservableList;
import com.wjb.o2o.entity.HeadLine;
import com.wjb.o2o.entity.ShopCategory;
import com.wjb.o2o.service.HeadLineService;
import com.wjb.o2o.service.ShopCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MainPageController {

    @Resource
    private ShopCategoryService shopCategoryService;

    @Resource
    private HeadLineService headLineService;

    @GetMapping("/listmainpageinfo")
    @ResponseBody
    private Map<String, Object> listMainPageInfo() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        try{
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
            modelMap.put("shopCategoryList", shopCategoryList);
        }catch(Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        List headLineList = new ArrayList<HeadLine>();
        try{
            HeadLine headLineCondition = new HeadLine();
            headLineCondition.setEnableStatus(1);
            headLineList = headLineService.getHeadLineList(headLineCondition);
            modelMap.put("headLineList", headLineList);
        }catch(Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }
}
