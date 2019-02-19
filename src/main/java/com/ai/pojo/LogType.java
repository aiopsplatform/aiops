package com.ai.pojo;

public class LogType {

    private Integer index_id;
    private String index_name;

    public LogType(Integer index_id, String index_name) {
        this.index_id = index_id;
        this.index_name = index_name;
    }

    public Integer getIndex_id() {
        return index_id;
    }

    public void setIndex_id(Integer index_id) {
        this.index_id = index_id;
    }

    public String getIndex_name() {
        return index_name;
    }

    public void setIndex_name(String index_name) {
        this.index_name = index_name;
    }
}
