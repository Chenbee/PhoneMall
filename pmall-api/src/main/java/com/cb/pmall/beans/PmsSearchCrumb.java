package com.cb.pmall.beans;

import java.io.Serializable;

/**
 * @author 陈彬
 * date 2020/4/20 11:11 PM
 */
public class PmsSearchCrumb implements Serializable {
    private String valueId;
    private String valueName;
    private String urlParam;

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }
}
