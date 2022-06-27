package com.micropos.delivery.models;

import java.io.Serializable;

public class DeliveryRecord implements Serializable {
    private Order order;
    private String status;
    private Long createdAt;

    public DeliveryRecord(Order order, String status, Long createdAt) {
        this.order = order;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "DeliveryRecord{" +
            "order=" + order +
            ", status='" + status + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
