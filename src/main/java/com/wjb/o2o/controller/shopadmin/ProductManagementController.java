package com.wjb.o2o.controller.shopadmin;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjb.o2o.dto.ImageHolder;
import com.wjb.o2o.dto.ProductExecution;
import com.wjb.o2o.entity.Product;
import com.wjb.o2o.entity.ProductCategory;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.enums.ProductStateEnum;
import com.wjb.o2o.service.ProductCategoryService;
import com.wjb.o2o.service.ProductService;
import com.wjb.o2o.utill.CodeUtil;
import com.wjb.o2o.utill.HttpServletRequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {

    @Resource
    private ProductService productService;

    @Resource
    private ProductCategoryService productCategoryService;

    private static final int IMAGEMAXCOUNT = 6;

    @PostMapping("/addproduct")
    @ResponseBody
    private Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //接受前端参数的变化的初始化，商品缩略图
        ObjectMapper mapper = new ObjectMapper();
        //要添加的商品
        Product product = null;
        //获取前端的product
        String productStr = HttpServletRequestUtils.getString(request, "productStr");
        //需要把request转换成MultipartHttpServletRequest，来取出图片
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        //用来存储缩略图和名字，是我们封装的简易类
        ImageHolder thumbnail = null;
        //用来存储多个详情图
        List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        try {
            //若请求中存在文件，则取出
            if (multipartResolver.isMultipart(request)) {
                handleImage((MultipartHttpServletRequest) request, productImgList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        try {
            //mapper使用来将前端传过来的表单string转换成xx.class实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (product != null && thumbnail != null && productImgList.size() > 0) {
            try {
                //从session中获取当前店铺id
                Shop currentShopshop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShopshop);
                //执行添加操作
                ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        }
        return modelMap;
    }

    @GetMapping("/getproductbyid")
    @ResponseBody
    private Map<String, Object> getProductById(@RequestParam Long productId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (productId > -1) {
            Product product = productService.getProductById(productId);
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product", product);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty productId");
        }
        return modelMap;
    }



    @PostMapping("/modifyproduct")
    @ResponseBody
    private Map<String, Object> ModifyProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 是商品编辑时候调用还是上下架操作的时候调用
        // 若为前者则进行验证码判断，后者则跳过验证码判断
        boolean statusChange = HttpServletRequestUtils.getBoolean(request, "statusChange");
        // 验证码判断
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        // 接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // 若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
      try{
          if (multipartResolver.isMultipart(request)) {
              handleImage((MultipartHttpServletRequest) request, productImgList);
          }
      }catch(Exception e){

              modelMap.put("success", false);
              modelMap.put("errMsg", e.toString());
              return modelMap;
      }
        try {
            String productStr = HttpServletRequestUtils.getString(request, "productStr");
            // 尝试获取前端传过来的表单string流并将其转换成Product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        // 非空判断
        if (product != null) {
            try {
                // 从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                // 开始进行商品信息变更操作
                ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

    private void handleImage(MultipartHttpServletRequest request, List<ImageHolder> productImgList) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest= request;
        //取出缩略图并构建IMAGEHolder对象
        MultipartFile thumbnailFile = multipartHttpServletRequest.getFile("thumbnail");
        ImageHolder imageHolder = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        for (int i = 0; i < IMAGEMAXCOUNT; i++) {
            MultipartFile imgFile = multipartHttpServletRequest.getFile("productImg" + i);
            if(imgFile!=null){
                ImageHolder productImg = new ImageHolder(imgFile.getOriginalFilename(), imgFile.getInputStream());
                productImgList.add(productImg);
            }else {
                break;
            }
        }
    }

    @GetMapping("/getproductlistbyshop")
    @ResponseBody
    private Map<String, Object> getProductListByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttpServletRequestUtils.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtils.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            Long productCategoryId = HttpServletRequestUtils.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtils.getString(request, "productName");
            Product product = compactProductCondition(currentShop.getShopId(), productCategoryId, productName);
            ProductExecution pe = productService.getProductList(product, pageIndex, pageSize);
            modelMap.put("productList", pe.getProductList());
            modelMap.put("count", pe.getCount());
            modelMap.put("success", true);
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    private Product compactProductCondition(Long shopId, Long productCategoryId, String productName) {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        product.setShop(shop);
        if (productCategoryId != -1L) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            product.setProductCategory(productCategory);
        }
        if (productName != null) {
            product.setProductName(productName);
        }
        return product;
    }
}
