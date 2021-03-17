package co.ex.coffeeforcodeapp.Api.Products;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MenuService {
    @GET("{cd_prod}")
    Call<DtoMenuById> getProductByCd (@Path("cd_prod") int cd_prod);
}
