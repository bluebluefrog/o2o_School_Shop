package com.wjb.o2o.mapper;

import com.wjb.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeadLineMapper {
    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLine);
}

