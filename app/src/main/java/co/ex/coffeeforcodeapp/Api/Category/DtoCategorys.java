package co.ex.coffeeforcodeapp.Api.Category;

import android.graphics.Bitmap;

public class DtoCategorys {
    int cd_cat;
    String nm_cat;
    Bitmap img_cat;

    public DtoCategorys(){}

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
