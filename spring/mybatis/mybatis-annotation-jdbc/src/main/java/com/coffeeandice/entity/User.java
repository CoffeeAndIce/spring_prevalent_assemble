package com.coffeeandice.entity;

import java.io.Serializable;

/**
 * author : CoffeeAndIce
 */
public class User implements Serializable {
    private Long id;

    private String name;

    private Integer no;

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

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }
}
