package co.ex.coffeeforcodeapp.Api.ShoppingCart;

import android.graphics.Bitmap;

public class DtoShoppingCart {
    int cd_prod, qt_prod, length;
    String email_user, nm_prod, img_prod;
    Bitmap img_prod_cart;
    float full_price_prod;
    float price_unit_prod;

    public DtoShoppingCart(){}

    public Bitmap getImg_prod_cart() {
        return img_prod_cart;
    }

    public void setImg_prod_cart(Bitmap img_prod_cart) {
        this.img_prod_cart = img_prod_cart;
    }

    public float getPrice_unit_prod() {
        return price_unit_prod;
    }

    public void setPrice_unit_prod(float price_unit_prod) {
        this.price_unit_prod = price_unit_prod;
    }

    public float getFull_price_prod() {
        return full_price_prod;
    }

    public void setFull_price_prod(float full_price_prod) {
        this.full_price_prod = full_price_prod;
    }

    public String getNm_prod() {
        return nm_prod;
    }

    public void setNm_prod(String nm_prod) {
        this.nm_prod = nm_prod;
    }

    public String getImg_prod() {
        return img_prod;
    }

    public void setImg_prod(String img_prod) {
        this.img_prod = img_prod;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

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
