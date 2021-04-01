package co.ex.coffeeforcodeapp.Api.ShoppingCart;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ShoppingCartService {

    @POST("insert/{id_user}/{email_user}/{cd_prod}/{nm_prod}/{img_prod}/{qt_prod}/{price_unit_prod}/{full_price_prod}")
    Call<DtoShoppingCart> insertItem(@Path("id_user") int id_user, @Path("email_user") String email_user, @Path("cd_prod") int cd_prod, @Path("nm_prod") String nm_prod,
                                     @Path("img_prod") String ImageUrl, @Path("qt_prod") int qt_prod, @Path("price_unit_prod") float price_unit_prod,
                                     @Path("full_price_prod") float full_price_prod);

    @GET("{email_user}")
    Call<DtoShoppingCart> getCartInfomration (@Path("email_user") String email_user);

    @PATCH("updatecart/{email_user}/{cd_prod}/{qt_prod}/{full_price_prod}")
    Call<DtoShoppingCart> updateItem(@Path("email_user") String email_user, @Path("cd_prod") int cd_prod, @Path("qt_prod") int qt_prod, @Path("full_price_prod") float full_price_prod);
}
