package com.wjb.o2o.mapper;

import com.wjb.o2o.BaseTest;
import com.wjb.o2o.entity.HeadLine;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HeadLineDaoTest extends BaseTest {
    @Resource
    private HeadLineMapper headLineMapper;

    @Test
    public void testQueryArea() {
        List<HeadLine> headLines = headLineMapper.queryHeadLine(new HeadLine());
        assertEquals(4, headLines.size());
    }
}
