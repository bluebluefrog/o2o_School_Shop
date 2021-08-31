package com.wjb.o2o.service.impl;

import com.wjb.o2o.mapper.AreaMapper;
import com.wjb.o2o.entity.Area;
import com.wjb.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaDao;

    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}
