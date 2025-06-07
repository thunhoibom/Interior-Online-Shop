package com.example.InteriorsECM.constants;

public class OrderConstants {

    public enum OrderStatus {
        PENDING("Pending"),
        PROCESSING("Processing"),
        SHIPPED("Shipped"),
        DELIVERED("Delivered"),
        CANCELLED("Cancelled");

        private final String value;

        OrderStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PaymentMethod {
        CREDIT_CARD("Credit Card"),
        PAYPAL("PayPal"),
        CASH_ON_DELIVERY("CASH_ON_DELIVERY"),
        BANK_TRANSFER("BANK_TRANSFER");

        private final String value;

        PaymentMethod(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PaymentStatus {
        PENDING("Pending"),
        PAID("Paid"),
        FAILED("Failed"),
        REFUNDED("Refunded");

        private final String value;

        PaymentStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ShippingMethod {
        STANDARD("Standard Shipping"),
        EXPRESS("Express"),
        DHL("DHL"),
        FEDEX("FedEx");

        private final String value;

        ShippingMethod(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}