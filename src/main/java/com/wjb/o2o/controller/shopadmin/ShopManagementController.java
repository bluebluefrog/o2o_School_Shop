package com.wjb.o2o.controller.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjb.o2o.dto.ImageHolder;
import com.wjb.o2o.dto.ShopExecution;
import com.wjb.o2o.entity.Area;
import com.wjb.o2o.entity.PersonInfo;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.entity.ShopCategory;
import com.wjb.o2o.enums.ShopStateEnum;
import com.wjb.o2o.service.AreaService;
import com.wjb.o2o.service.ShopCategoryService;
import com.wjb.o2o.service.ShopService;
import com.wjb.o2o.utill.CodeUtil;
import com.wjb.o2o.utill.HttpServletRequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Resource
    private ShopService shopService;

    @Resource
    private ShopCategoryService shopCategoryService;

    @Resource
    private AreaService areaService;

    @GetMapping("/getshopmanagementinfo")
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        long shopId = HttpServletRequestUtils.getLong(request, "shopId");
        if (shopId <= 0) {
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
            }else{
                Shop currentShop=(Shop)currentShopObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        }else{
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);
            modelMap.put("shopId", shopId);
        }
        return modelMap;
    }

    @GetMapping("/getshoplist")
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<String, Object>();

        //因为还没有开发登录功能，等下修改
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        request.getSession().setAttribute("user",user);
        user = (PersonInfo) request.getSession().getAttribute("user");

        long employeeId= user.getUserId();
        try{
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopList(shopCondition, 0, 100);

            modelMap.put("shopList", se.getShopList());
            modelMap.put("user", user);
            modelMap.put("success", true);
        }catch(Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @GetMapping("/getshopbyid")
    @ResponseBody
    private Map<String,Object>getShopById(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long shopId = HttpServletRequestUtils.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            }catch (Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @GetMapping("/getshopinitinfo")
    @ResponseBody
    private Map<String,Object>getShopInitInfo(){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();
        try{
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("success", true);
        }catch(Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        //定义返回的map
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "错误验证码");
            return modelMap;
        }
        //1.接受并转化相应的参数，包括店铺信息以及图片信息
        //从request中获取需要使用的json的key，然后进行对应类型转换（这里是字符串）
        String shopStr = HttpServletRequestUtils.getString(request, "shopStr");
        //使用jakson-databind需要new一个ObjectMapper来转化
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;
        try {
            //进行转换，前面是需要转换的json信息，后面是实体类.class
            shop = objectMapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            //抛出转换时异常
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        //处理图片相关逻辑
        CommonsMultipartFile shopImg = null;//接收图片，使用CommonsMultipartFile
        //需要new出文件上传解析器CommonsMultipartResolver，在sessionContext中获取内容，用来解析request中的图片信息
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查request中是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)) {
            //如果有需要对request进行转换来提取文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //提取文件流将其转换成CommonsMultipartFile，然后可以交给我们之前定义的工具进行处理
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }else{
            //没有文件流进行报错
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        //2.注册店铺
        if (shop != null && shopImg != null) {
            //从Session中获取owner
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution se= null;
            try {
                ImageHolder img = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                se = shopService.addShop(shop,img);
                if (se.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList",shopList);
                    //上面的逻辑是，判断用户可以修改操作的店铺，将他的店铺装进一个列表，放入session，只在session有效期有用，省去从数据库中查询的时间
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                    return modelMap;
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
            return modelMap;
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }

    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        //定义返回的map
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "错误验证码");
            return modelMap;
        }
        //1.接受并转化相应的参数，包括店铺信息以及图片信息
        //从request中获取需要使用的json的key，然后进行对应类型转换（这里是字符串）
        String shopStr = HttpServletRequestUtils.getString(request, "shopStr");
        //使用jakson-databind需要new一个ObjectMapper来转化
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;
        try {
            //进行转换，前面是需要转换的json信息，后面是实体类.class
            shop = objectMapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            //抛出转换时异常
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        //处理图片相关逻辑
        CommonsMultipartFile shopImg = null;//接收图片，使用CommonsMultipartFile
        //需要new出文件上传解析器CommonsMultipartResolver，在sessionContext中获取内容，用来解析request中的图片信息
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查request中是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)) {
            //如果有需要对request进行转换来提取文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //提取文件流将其转换成CommonsMultipartFile，然后可以交给我们之前定义的工具进行处理
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //这里去掉图片上传判断，可以不上传图片
        //2.更改店铺信息
        if (shop != null && shop.getShopId()!=null) {//在更改店铺信息时要确定有shopId而不需要上传图片
            //修改店铺信息不需要获取用户信息
            ShopExecution se=null;
            try {
                if(shopImg==null){
                    se = shopService.modifyShop(shop,null);//如果图片为空需要传入null
                }
                ImageHolder img = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                se = shopService.modifyShop(shop,img);
                if (se.getState() == ShopStateEnum.SUCCESS.getState()) {//更改信息如果对应成功则成功
                    modelMap.put("success", true);
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                    return modelMap;
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
            return modelMap;
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺id");
            return modelMap;
        }
    }
}
