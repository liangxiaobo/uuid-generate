package com.lb.uuid.common.uuidcommon.model;

import java.io.Serializable;

/**
 * @program: lb-uuid-parent
 * @description: 存储 {workId:1, datacenterId:1}
 * @author: liangbo
 * @create: 2019-06-04 12:39
 **/
public class UuidDataItemModel implements Serializable {

    private static final long serialVersionUID = 8302906680567695946L;

    private int workId;
    private int dataCenterId;

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public int getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(int dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public UuidDataItemModel(int workId, int dataCenterId) {
        this.workId = workId;
        this.dataCenterId = dataCenterId;
    }

    public UuidDataItemModel() {
    }

    @Override
    public String toString() {
        return "UuidDataItemModel{" +
                "workId=" + workId +
                ", dataCenterId=" + dataCenterId +
                '}';
    }
}
