package co.ex.coffeeforcodeapp.Api.ShoppingCart;

public class DtoShoppingCart {
    int cd_prod, qt_prod;
    String email_user;

    public DtoShoppingCart(){}

    public int getCd_prod() {
        return cd_prod;
    }

    public void setCd_prod(int cd_prod) {
        this.cd_prod = cd_prod;
    }

    public int getQt_prod() {
        return qt_prod;
    }

    public void setQt_prod(int qt_prod) {
        this.qt_prod = qt_prod;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }
}
