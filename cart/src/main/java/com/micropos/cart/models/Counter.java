package com.micropos.cart.models;

import java.util.List;

public class Counter {
    List<PriceEntry> items;
    double total;

    public Counter(List<PriceEntry> items, double total) {
        this.items = items;
        this.total = total;
    }

    public List<PriceEntry> getItems() {
        return items;
    }

    public void setItems(List<PriceEntry> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
