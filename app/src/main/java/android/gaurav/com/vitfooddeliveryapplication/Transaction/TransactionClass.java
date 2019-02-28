package android.gaurav.com.vitfooddeliveryapplication.Transaction;

public class TransactionClass {
    double amount;
    String  toEmail;
    String orderID, date, time;
    String name;

    public TransactionClass() {}

    public TransactionClass(Double amount, String toEmail, String orderID, String date, String time, String name)
    {
        this.amount = amount;
        this.toEmail = toEmail;
        this.orderID = orderID;
        this.date = date;
        this.time = time;
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
