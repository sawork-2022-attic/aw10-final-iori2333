package com.micropos.cart.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private final List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    public void addItem(Item item) {
        items.stream()
            .filter(it -> it.getProductId().equals(item.getProductId()))
            .findAny()
            .ifPresentOrElse(
                it -> it.setQuantity(it.getQuantity() + item.getQuantity()),
                () -> items.add(item)
            );
    }

    public void removeItem(Item item) {
        items.stream()
            .filter(it -> it.getProductId().equals(item.getProductId()))
            .findAny()
            .ifPresent(it -> {
                if (it.getQuantity() > item.getQuantity()) {
                    it.setQuantity(it.getQuantity() - item.getQuantity());
                } else {
                    items.remove(it);
                }
            });
    }

    public void modifyItem(Item item) {
        items.stream()
            .filter(it -> it.getProductId().equals(item.getProductId()))
            .findAny()
            .ifPresentOrElse(
                it -> it.setQuantity(item.getQuantity()),
                () -> items.add(item)
            );
    }
}
