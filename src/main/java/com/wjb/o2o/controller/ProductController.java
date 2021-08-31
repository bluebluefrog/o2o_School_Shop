package com.wjb.o2o.controller;

import com.wjb.o2o.entity.Product;
import com.wjb.o2o.service.ProductService;
import com.wjb.o2o.utill.HttpServletRequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("listproductdetailpageinfo")
    @ResponseBody
    private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long productId = HttpServletRequestUtils.getLong(request, "productId");
        Product product = null;

        if (productId != -1) {
            product = productService.getProductById(productId);
            modelMap.put("product", product);
            modelMap.put("success", true);
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty productId");
        }
        return modelMap;
    }
}
