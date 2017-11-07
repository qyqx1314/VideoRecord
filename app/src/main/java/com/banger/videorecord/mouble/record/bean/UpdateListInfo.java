package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhusiliang on 16/6/29.
 */
public class UpdateListInfo implements Serializable {
    private List<LocalRecordBean> list;

    public List<LocalRecordBean> getList() {
        if (null==list){
            list = new ArrayList<>();
        }
        return list;
    }

    public void setList(List<LocalRecordBean> list) {
        this.list = list;
    }
}
