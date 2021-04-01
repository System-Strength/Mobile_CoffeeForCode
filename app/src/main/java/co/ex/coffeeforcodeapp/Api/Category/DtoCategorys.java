package co.ex.coffeeforcodeapp.Api.Category;

import android.graphics.Bitmap;

public class DtoCategorys {
    int cd_cat;
    String nm_cat, img_cat_st;
    Bitmap img_cat;

    public DtoCategorys(){}

    public String getImg_cat_st() {
        return img_cat_st;
    }

    public void setImg_cat_st(String img_cat_st) {
        this.img_cat_st = img_cat_st;
    }

    public int getCd_cat() {
        return cd_cat;
    }

    public void setCd_cat(int cd_cat) {
        this.cd_cat = cd_cat;
    }

    public String getNm_cat() {
        return nm_cat;
    }

    public void setNm_cat(String nm_cat) {
        this.nm_cat = nm_cat;
    }

    public Bitmap getImg_cat() {
        return img_cat;
    }

    public void setImg_cat(Bitmap img_cat) {
        this.img_cat = img_cat;
    }
}
