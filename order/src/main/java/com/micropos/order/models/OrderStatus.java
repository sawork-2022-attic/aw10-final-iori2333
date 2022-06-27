package com.micropos.order.models;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    ToSend,
    ToCancel,
    Delivering,
    Completed,
    Canceled
}
