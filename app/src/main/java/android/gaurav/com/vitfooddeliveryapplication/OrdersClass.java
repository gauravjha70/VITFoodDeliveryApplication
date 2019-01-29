package android.gaurav.com.vitfooddeliveryapplication;

import java.sql.Time;

public class OrdersClass {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public class OrderDetailsClass{
        private String service, deliveryAddress, orderStatus;
        private Time timing, deliveryTimming;
        private Double price;

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

        public Time getTimming() {
            return timing;
        }

        public void setTimming(Time timing) {
            this.timing = timing;
        }

        public Time getDeliveryTimming() {
            return deliveryTimming;
        }

        public void setDeliveryTimming(Time deliveryTimming) {
            this.deliveryTimming = deliveryTimming;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }
}
