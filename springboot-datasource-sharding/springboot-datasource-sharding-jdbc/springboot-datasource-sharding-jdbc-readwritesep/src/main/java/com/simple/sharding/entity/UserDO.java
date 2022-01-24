package com.simple.sharding.entity;

/**
 * 订单 DO
 */
public class UserDO {

    /**
     * 订单编号
     */
    private Long id;
    /**
     * 用户编号
     */
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "OrderDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}