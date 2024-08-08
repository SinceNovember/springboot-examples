package com.simple.entity;

public class Part {

    private Integer id;

    private String partName;

    public Part(Integer id, String partName) {
        this.id = id;
        this.partName = partName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }
}
