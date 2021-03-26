package ru.slava62;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import retrofit2.Response;
import ru.slava62.allure.env.EnvironmentInfo;
import ru.slava62.dto.Category;
import ru.slava62.dto.ErrorBody;
import ru.slava62.dto.Product;
import ru.slava62.service.CategoryService;
import ru.slava62.service.ProductService;
import ru.slava62.util.RetrofitUtils;
import java.util.List;
import java.util.Random;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


@Epic("Market tests")
@Feature("Product test-suite")
public class ProductTests {
    private static final Long NO_EXISTING_PRODUCT_ID = -1l;
    String categoryTitle;
    Product productForTest;
    static ProductService productService;
    static CategoryService categoryService;
    Faker faker = new Faker();

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getProductService();
        categoryService=RetrofitUtils.getCategoryService();
    }
    @SneakyThrows
    @BeforeEach
    void setUp() {
        Response<Category> response= RetrofitUtils.getCategoryResponse((int)(Math.random() * 2 + 1),categoryService);
        if(response.isSuccessful()) {
        List<Product> products=response.body().getProducts();
        productForTest= products.get(new Random().nextInt(products.size()));
        categoryTitle = response.body().getTitle();}
    }

    @SneakyThrows
    @RepeatedTest(value=4,name="Get product with id")
    @DisplayName("Get product by id test")
    @Description("Check the product with id is available")
    void getProductTest() {
        Response<Product> response=RetrofitUtils.getProductResponse(productForTest.getId(),productService);
        step("Check successful response");
        assertThat(response.isSuccessful(), is(true));
        step("Check the product with id: "+productForTest.getId()+" is available");
        assertThat(productForTest.getId(),equalTo(response.body().getId()));
        step("Check the title of product is "+productForTest.getTitle());
        assertThat(productForTest.getTitle(),equalTo(response.body().getTitle()));
        step("Check the price of product is "+productForTest.getPrice().toString());
        assertThat(productForTest.getPrice(),equalTo(response.body().getPrice()));
    }
    @SneakyThrows
    @Test
    @DisplayName("Get product no existing id test")
    @Description("Check that the product with id no existing in database can't be returned")
    void getNotExistingProductTest() {
        Response<Product> response=RetrofitUtils.getProductResponse(NO_EXISTING_PRODUCT_ID,productService);
        step("Check unsuccessful response");
        assertThat(response.isSuccessful(), is(false));
        step("Check response code 404");
        assertThat(response.code(), is(404));
        step("Check response error is \"Unable to find product with id: " +
                NO_EXISTING_PRODUCT_ID + "\"");
        assertThat(ErrorBody.getJsonErrorMessage(response).getString("message"),
                is(equalTo("Unable to find product with id: " +
                        NO_EXISTING_PRODUCT_ID)));
    }
    @SneakyThrows
    @Test
    @DisplayName("Update not existing product test")
    @Description("Check that the product with id no existing in database can't be updated")
    void updateNotExistingProductTest() {
        Product product=new Product(-1L,
                faker.food().ingredient(),(int) (Math.random() * 1000 + 1),
                categoryTitle);
        Response<Product> response=RetrofitUtils.updateProductResponse(product,productService);
        step("Check unsuccessful response");
        assertThat(response.isSuccessful(), is(false));
        step("Check response code 400");
        assertThat(response.code(), is(400));
        step("Check response error is \"Product with id: " +
                NO_EXISTING_PRODUCT_ID + " doesn't exist\"");
        assertThat(ErrorBody.getJsonErrorMessage(response).getString("message"),
                is(equalTo("Product with id: " +
                        NO_EXISTING_PRODUCT_ID + " doesn't exist")));
    }
    @SneakyThrows
    @Test
    @DisplayName("Update existing product test")
    @Description("Check that the product was updated")
    void updateProductTest() {
        Product product=new Product(productForTest.getId(),
                faker.food().ingredient(),(int) (Math.random() * 1000 + 1),
                categoryTitle);
        Response<Product> response=RetrofitUtils.updateProductResponse(product,productService);
        step("Check response code 200");
        assertThat(response.code(), is(200));
        step("Check the product attribute \"title\"");
        assertThat(product.getTitle(),
                is(equalTo(response.body().getTitle())));
        step("Check the product attribute \"price\"");
        assertThat(product.getPrice(),
                is(equalTo(response.body().getPrice())));
        step("Check the product attribute \"categoryTitle\"");
        assertThat(product.getTitle(),
                is(equalTo(response.body().getTitle())));
        if(response.isSuccessful()) {
            step("Update to old attributes of product");
            assertThat(RetrofitUtils.updateProductResponse(productForTest,
                    productService).code(), is(200));
        }
    }
    @SneakyThrows
    @ParameterizedTest
    @MethodSource(value = "ru.slava62.data.TestData#productDataProvider")
    @DisplayName("Create new product test")
    @Description("Check that the product was created")
    void createNewProductTest(String productTitle,Integer productPrice, String categoryTitle) {
     Product product=new Product(null,
             productTitle,productPrice,categoryTitle);
     Response<Product> response=RetrofitUtils.createProductResponse(product,productService);
     step("Check response code 201");
     assertThat(response.code(), is(equalTo(201)));
     step("Check the product with id: "+response.body().getId()+" title is "+productTitle);
             assertThat(productTitle,
             is(equalTo(response.body().getTitle())));
     step("Check the product with id: "+response.body().getId()+" price is "+productPrice);
     assertThat(productPrice,
             is(equalTo(response.body().getPrice())));
     step("Check the product with id: "+response.body().getId()+" category title is "+categoryTitle);
     assertThat(categoryTitle,
             is(equalTo(response.body().getCategoryTitle())));
             if(response.isSuccessful())
                 step("Check successful deleting after creating");
                 assertThat(RetrofitUtils.deleteProductResponse(response.body().getId()
                         ,productService).code(), is(equalTo(200)));
    }
    @SneakyThrows
    @Test
    @DisplayName("Create new product with id test")
    @Description("Check that the product with id can't be created")
    void createNewProductWithIdTest() {
        Product product=new Product(productForTest.getId(),
                productForTest.getTitle(),productForTest.getPrice(),
                productForTest.getCategoryTitle());
        Response<Product> response=RetrofitUtils.createProductResponse(product,productService);
        step("Check response code 400");
        assertThat(response.code(), is(equalTo(400)));
        step("Check the error message is \"Id must be null for new entity\"");
        assertThat(ErrorBody.getJsonErrorMessage(response).getString("message"),
                is(equalTo("Id must be null for new entity")));
        if(response.isSuccessful())
            assertThat(RetrofitUtils.deleteProductResponse(response.body().getId()
                    ,productService).code(), is(equalTo(200)));
    }
    @SneakyThrows
    @Test
    @DisplayName("Delete not existing product test")
    @Description("Check that the product was deleted from database can't be updated")
    void deleteNotExistingProductTest() {
        Response<ResponseBody> response=RetrofitUtils.deleteProductResponse(NO_EXISTING_PRODUCT_ID,productService);
        step("Check unsuccessful response");
        assertThat(response.isSuccessful(), is(false));
        step("Check response code 500");
        assertThat(response.code(), is(500));
        step("Check the error message is \"Internal Server Error\"");
        assertThat(ErrorBody.getJsonErrorMessage(response).getString("error"),
                is(equalTo("Internal Server Error")));
    }
    @SneakyThrows
    @Test
    @DisplayName("Get all of products test")
    @Description("Check that the product returns error code 500")
    void getAllProductsTest() {
        Response<ResponseBody> response=RetrofitUtils.getAllProductsResponse(productService);
        step("Check response code 500");
        assertThat(response.code(),is(equalTo(500)));
        step("Check the error message is \"Internal Server Error\"");
        assertThat(ErrorBody.getJsonErrorMessage(response).getString("error"),
                is(equalTo("Internal Server Error")));
    }

    @AfterAll
    static void afterAll() {
        EnvironmentInfo.setAllureEnvironment();
    }
}
