package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhusiliang on 16/9/26.
 */
public class CustomerInfoBean  implements Serializable {
    private List<FiledInfo> templateFields;
    private List<TypeInfo> identifyType;
    private List<TypeInfo> riskLevel;

    public List<FiledInfo> getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(List<FiledInfo> templateFields) {
        this.templateFields = templateFields;
    }

    public List<TypeInfo> getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(List<TypeInfo> identifyType) {
        this.identifyType = identifyType;
    }

    public List<TypeInfo> getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(List<TypeInfo> riskLevel) {
        this.riskLevel = riskLevel;
    }

    @Override
    public String toString() {
        return "CustomerInfoBean{" +
                "templateFields=" + templateFields +
                ", identifyType=" + identifyType +
                ", riskLevel=" + riskLevel +
                '}';
    }
}
