package com.wjb.o2o.service;

import com.wjb.o2o.BaseTest;
import com.wjb.o2o.dto.ImageHolder;
import com.wjb.o2o.dto.ProductExecution;
import com.wjb.o2o.entity.Area;
import com.wjb.o2o.entity.Product;
import com.wjb.o2o.entity.ProductCategory;
import com.wjb.o2o.entity.Shop;
import com.wjb.o2o.enums.ProductStateEnum;
import com.wjb.o2o.exceptions.ShopOperationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    public void testGetAreaList(){
        List<Area> areaList = areaService.getAreaList();
        assertEquals("南苑", areaList.get(0).getAreaName());
    }

}
