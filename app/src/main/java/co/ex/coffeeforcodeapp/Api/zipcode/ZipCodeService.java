package co.ex.coffeeforcodeapp.Api.zipcode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ZipCodeService {

    @GET("{zipcode}/json/")
    Call<DtoZipCode> getAddress(@Path("zipcode") String zipcode);
}
