package com.wjb.o2o.utill;

public class PageCalculator {
    public static int calculateRowIndex(int pageIndex,int pageSize){
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }
}
