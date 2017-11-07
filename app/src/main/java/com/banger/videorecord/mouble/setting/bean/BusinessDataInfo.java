package com.banger.videorecord.mouble.setting.bean;

import com.banger.videorecord.mouble.product.bean.BizType;
import com.banger.videorecord.mouble.product.bean.ProdClass;
import com.banger.videorecord.mouble.record.bean.FiledInfo;
import com.banger.videorecord.mouble.record.bean.ProcessInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhusiliang on 16/6/18.
 */
public class BusinessDataInfo implements Serializable{
    private BizType bizType;
    private ArrayList<ProdClass> prodClass;
    private List<FiledInfo> templateFields;//录像的自定义字段集合
    private List<ProcessInfo> processes;//录像环节的自定义字段

    public List<ProcessInfo> getProcesses() {
        return processes;
    }

    public void setProcesses(List<ProcessInfo> processes) {
        this.processes = processes;
    }

    public List<FiledInfo> getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(List<FiledInfo> templateFields) {
        this.templateFields = templateFields;
    }

    public BizType getBizType() {
        return bizType;
    }

    public void setBizType(BizType bizType) {
        this.bizType = bizType;
    }

    public ArrayList<ProdClass> getProdClass() {
        return prodClass;
    }

    public void setProdClass(ArrayList<ProdClass> prodClass) {
        this.prodClass = prodClass;
    }

    @Override
    public String toString() {
        return "BusinessDataInfo{" +
                "bizType=" + bizType +
                ", prodClass=" + prodClass +
                '}';
    }
}
