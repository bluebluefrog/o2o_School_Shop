package com.wjb.o2o.service;

import com.wjb.o2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

public interface HeadLineService {

    List<HeadLine> getHeadLineList(HeadLine headLineCondition)throws IOException;
}
