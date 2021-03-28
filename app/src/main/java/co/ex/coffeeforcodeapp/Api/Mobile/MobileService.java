package co.ex.coffeeforcodeapp.Api.Mobile;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MobileService {

    @GET("version")
    Call<DtoMobile> getMobileVersion();
}
