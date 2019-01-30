package android.gaurav.com.vitfooddeliveryapplication;

import java.io.Serializable;
import java.sql.Time;

public class OrdersClass implements Serializable {
    private String email;
    private String name;
    private String service, deliveryAddress, orderStatus, message;
    private String timing, deliveryTimming;
    private Double price;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


    public String getDeliveryTimming() {
        return deliveryTimming;
    }

    public void setDeliveryTimming(String deliveryTimming) {
        this.deliveryTimming = deliveryTimming;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

