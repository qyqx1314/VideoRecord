package com.banger.videorecord.mouble.setting.bean;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/6/18.
 */
public class PageBean  implements Serializable{
    private int  total;
    private int  pageSize;
    private int  pageNum;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
