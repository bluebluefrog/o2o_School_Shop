package com.wjb.o2o.mapper;

import com.wjb.o2o.BaseTest;
import com.wjb.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaDaoTest extends BaseTest {
    @Autowired
    private AreaMapper areaDao;

    @Test
    public void testQueryArea() {
        List<Area> areaList = areaDao.queryArea();
        assertEquals(3, areaList.size());
    }
}
