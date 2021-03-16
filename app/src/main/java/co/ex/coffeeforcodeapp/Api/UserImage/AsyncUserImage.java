package co.ex.coffeeforcodeapp.Api.UserImage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import java.io.IOException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("StaticFieldLeak")
public class AsyncUserImage extends AsyncTask {
    String img_user;
    CircleImageView icon_profileUser_principal;


    public AsyncUserImage(String img_user, CircleImageView icon_profileUser_principal) {
        this.img_user = img_user;
        this.icon_profileUser_principal = icon_profileUser_principal;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        icon_profileUser_principal.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Bitmap img_prod = null;
        try {
                URL url = new URL("https://coffeeforcode.herokuapp.com/" + img_user);
                img_prod = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException malformedURLException) {
            malformedURLException.printStackTrace();
        }
        return img_prod;
    }

    @Override
    protected void onPostExecute(Object img_prod) {
        super.onPostExecute(img_prod);
        icon_profileUser_principal.setImageBitmap((Bitmap) img_prod);
        icon_profileUser_principal.setVisibility(View.VISIBLE);

    }
}
