package com.wjb.o2o.controller.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "shopadmin")
public class ShopAdminController {

    @GetMapping("/shopoperation")
    public String shopOperation(){
        return "shop/shopoperation";
    }

    @GetMapping("/shoplist")
    public String shopList(){
        return "shop/shoplist";
    }

    @GetMapping("/shopmanagement")
    public String shopManagement(){
        return "shop/shopmanagement";
    }

    @GetMapping("/productcategorymanagement")
    public String productCategoryMange(){
        return "shop/productcategorymanagement";
    }

    @GetMapping("/productoperation")
    public String productOperationManage(){
        return "shop/productoperation";
    }

    @GetMapping("/productmanagement")
    public String productmanagement(){
        return "shop/productmanagement";
    }
}

