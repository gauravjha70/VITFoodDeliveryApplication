package android.gaurav.com.vitfooddeliveryapplication;


public class UserClass {


    private String email;
    private String regNo;
    private String name;
    private String mobileNumber;
    private Double credits;

    public UserClass(String email,String regNo, String name, String mobileNumber, Double credits)
    {
            this.email = email;
            this.regNo = regNo;
            this.name = name;
            this.mobileNumber = mobileNumber;
            this.credits = credits;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Double getcredits() {
        return credits;
    }

    public void setcredits(Double credits) {
        credits = credits;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
