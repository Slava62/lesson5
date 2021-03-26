package ru.slava62.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.slava62.dto.Category;
import ru.slava62.dto.Product;
import ru.slava62.service.CategoryService;
import ru.slava62.service.ProductService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;

@UtilityClass
public class RetrofitUtils {
   HttpLoggingInterceptor logging =  new HttpLoggingInterceptor(new PrettyLogger());

    public Retrofit getRetrofit(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofMinutes(1l))
                .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(ConfigUtils.getBaseUrl())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    public CategoryService getCategoryService() throws MalformedURLException{
       return getRetrofit().create(CategoryService.class);
    }

    public ProductService getProductService() throws MalformedURLException{
        return getRetrofit().create(ProductService.class);
    }

    public Response<Product> getProductResponse(Long productId, ProductService service) throws IOException {
        return service
                .getProduct(productId)
                .execute();
    }

    public Response<Product> createProductResponse(Product product, ProductService service) throws IOException {
        return service
                .createProduct(product)//(int)productId) long productId
                .execute();
    }

    public Response<Product> updateProductResponse(Product product, ProductService service) throws IOException {
        return service
                .updateProduct(product)//(int)productId) long productId
                .execute();
    }

    public Response<ResponseBody> deleteProductResponse(Long productId, ProductService service) throws IOException {
        return service
                .deleteProduct(productId)
                .execute();
    }

    public Response<Category> getCategoryResponse(Integer categoryId, CategoryService service) throws IOException {
      return service
                .getCategory(categoryId)
                .execute();
    }

    public Response<Category> updateCategoryResponse(Integer categoryId, CategoryService service) throws IOException {
        return service
                .updateCategory(categoryId)
                .execute();
    }

    public Response<Category> deleteCategoryResponse(Integer categoryId, CategoryService service) throws IOException {
        return service
                .deleteCategory(categoryId)
                .execute();
    }

    public Response<Category> createCategoryResponse(Category category, CategoryService service) throws IOException{
        return service
                .createCategory(category)
                .execute();
    }

    public static Response<ResponseBody> getAllProductsResponse(ProductService service) throws IOException {
        return service
                .getAllProducts()
                .execute();
    }
}
