package com.example.inventoryservice.dto;

import com.example.inventoryservice.entity.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ItemDto {
    private Long id;
    private Double price;
    private String name;
    private String note;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonIgnore
    public Item getItem(){
        Item retval = new Item();
        retval.setId(id);
        retval.setName(name);
        retval.setPrice(price);
        retval.setNote(note);
        retval.setUserId(userId);
        return retval;
    }

    public ItemDto(Item item) {
        if(item == null)
            return;
        id = item.getId();
        name = item.getName();
        price = item.getPrice();
        note = item.getNote();
        userId = item.getUserId();
    }
}
