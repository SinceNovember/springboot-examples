package com.simple.canal.sync.kafka.model;

public class TbCommodityInfo {

    private String id;
    private String commodity_name;
    private String commodity_price;
    private String number;
    private String description;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }
    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_price(String commodity_price) {
        this.commodity_price = commodity_price;
    }
    public String getCommodity_price() {
        return commodity_price;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getNumber() {
        return number;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
