package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/17.
 */
public class WorkOrderReview implements Serializable{

    public String sign;
    public String soundRecord;
    public Integer evaluate;
    public String remark;

//

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSoundRecord() {
        return soundRecord;
    }

    public void setSoundRecord(String soundRecord) {
        this.soundRecord = soundRecord;
    }

    public Integer getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Integer evaluate) {
        this.evaluate = evaluate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
