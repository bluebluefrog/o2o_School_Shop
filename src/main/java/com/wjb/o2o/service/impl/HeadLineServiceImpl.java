package com.wjb.o2o.service.impl;

import com.wjb.o2o.entity.HeadLine;
import com.wjb.o2o.mapper.HeadLineMapper;
import com.wjb.o2o.service.HeadLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Resource
    private HeadLineMapper headLineMapper;


    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
        return headLineMapper.queryHeadLine(headLineCondition);
    }
}
