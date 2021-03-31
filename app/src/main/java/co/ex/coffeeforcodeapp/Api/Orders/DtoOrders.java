package co.ex.coffeeforcodeapp.Api.Orders;


public class DtoOrders {
    int cd_order;
    String email_user, zipcode, address_user, complement, cd_prods, PayFormat_user, status, held_in;
    int length;


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getCd_order() {
        return cd_order;
    }

    public void setCd_order(int cd_order) {
        this.cd_order = cd_order;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress_user() {
        return address_user;
    }

    public void setAddress_user(String address_user) {
        this.address_user = address_user;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCd_prods() {
        return cd_prods;
    }

    public void setCd_prods(String cd_prods) {
        this.cd_prods = cd_prods;
    }

    public String getPayFormat_user() {
        return PayFormat_user;
    }

    public void setPayFormat_user(String payFormat_user) {
        PayFormat_user = payFormat_user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHeld_in() {
        return held_in;
    }

    public void setHeld_in(String held_in) {
        this.held_in = held_in;
    }
}
