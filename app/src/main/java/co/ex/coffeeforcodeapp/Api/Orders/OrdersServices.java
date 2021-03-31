package co.ex.coffeeforcodeapp.Api.Orders;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrdersServices {

    @GET("{email_user}")
    Call<DtoOrders> getOrdersbyUser(@Path("email_user") String email_user);

    @POST("insert/{email_user}/{zipcode}/{address_user}/{complement}/{PayFormat_user}/{status}")
    Call<DtoOrders> postOrder(@Path("email_user") String email_user, @Path("zipcode") String zipcode, @Path("address_user") String address_user,
                              @Path("complement") String complement, @Path("PayFormat_user") String PayFormat_user,
                              @Path("status") String status);

    @PATCH("update/{cd_order}/{status}")
    Call<DtoOrders> updateStatus( @Path("cd_order") int cd_orderAfter, @Path("status") String status);

    @GET("cd/{cd_order}")
    Call<DtoOrders> getOrderByCode( @Path("cd_order") int cd_order);
}
