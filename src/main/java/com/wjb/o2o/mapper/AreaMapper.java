package com.wjb.o2o.mapper;

import com.wjb.o2o.entity.Area;

import java.util.List;

public interface AreaMapper {
    /**
     * 列出地域列表
     * @return areaList
     */
    List<Area> queryArea();
}
