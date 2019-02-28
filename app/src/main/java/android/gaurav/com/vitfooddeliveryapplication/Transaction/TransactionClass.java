package android.gaurav.com.vitfooddeliveryapplication.Transaction;

public class TransactionClass {
    double amount;
    String fromEmail, toEmail;
    String orderID, date, time;
    String name;

    public TransactionClass(Double amount, String fromEmail, String toEmail, String orderID, String date, String time, String name)
    {
        this.amount = amount;
        this.fromEmail = fromEmail;
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

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
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
