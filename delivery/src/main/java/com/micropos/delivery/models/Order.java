package com.micropos.delivery.models;


import com.micropos.delivery.dto.ItemDto;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    String id;
    List<ItemDto> items;
    OrderStatus status;

    public Order(String id, List<ItemDto> items, OrderStatus status) {
        this.id = id;
        this.items = items;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
