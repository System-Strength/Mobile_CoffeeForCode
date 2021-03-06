package co.ex.coffeeforcodeapp.Api.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersService {

    @POST("register")
    Call<DtoUsers> registerNewUse(@Body DtoUsers users);

    @GET("login/{email}")
    Call<DtoUsers> loginUser(@Path("email") String email);

    @PATCH("updateaddress/{id_user}")
    Call<DtoUsers> UpdateAddress(@Path("id_user") int id_user, @Body DtoUsers newAddress);

    @PATCH("update/{id_user}")
    Call<DtoUsers> UpdateUser(@Path("id_user") int id_user, @Body DtoUsers newUserInfo);

    @PATCH("updateimg/{id_user}")
    Call<DtoUsers> UpdateImgUser(@Path("id_user") int id_user, @Body DtoUsers newImg);

    @GET("{email}")
    Call<DtoUsers> infoUser(@Path("email") String email);
}
