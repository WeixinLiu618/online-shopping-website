package com.shop.paymentservice.entity;

public enum PaymentStatus {
    PENDING,    // 初始状态，尚未付款
    PROCESSING, // 正在处理（比如第三方支付网关处理中）
    SUCCESS,    // 支付成功
    FAILED,     // 支付失败
    REFUNDED    // 已退款
}
