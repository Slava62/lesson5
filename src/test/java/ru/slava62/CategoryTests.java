package ru.slava62;

import static io.qameta.allure.Allure.step;
import com.github.javafaker.Faker;;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import retrofit2.Response;
import ru.slava62.allure.env.EnvironmentInfo;
import ru.slava62.dto.Category;
import ru.slava62.dto.ErrorBody;
import ru.slava62.service.CategoryService;
import ru.slava62.util.RetrofitUtils;
import java.util.ArrayList;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Market tests")
@Feature("Category test-suite")
public class CategoryTests {

   static CategoryService categoryService;
   static Category categoryForTest;

   Faker faker = new Faker();
    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        categoryService = RetrofitUtils.getCategoryService();
        categoryForTest=null;
    }
    @SneakyThrows
    @BeforeEach
    void setUp() {
        categoryForTest=RetrofitUtils.getCategoryResponse((int)(Math.random() * 3 + 1), categoryService).body();

       // categoryForTest.setTitle(faker.food().ingredient());

    }

    @SneakyThrows
    @RepeatedTest(value=3,name="Get category with id")
    @DisplayName("Get category by id test")
    @Description("Check the category with id is available")
    void getCategoryTest()  {
        Response<Category> response=RetrofitUtils.getCategoryResponse(categoryForTest.getId(), categoryService);
        step("Check successful response");
        assertThat(response.isSuccessful(),is(true));
        step("Check category exists");
        assertThat(categoryForTest.getId(),equalTo(response.body().getId()));
        step("Check title of category with id: "+categoryForTest.getId()+" is "+categoryForTest.getTitle());
        assertThat(categoryForTest.getTitle(),equalTo(response.body().getTitle()));
    }
    @SneakyThrows
    @ParameterizedTest
    @MethodSource(value = "ru.slava62.data.TestData#categoryDataProvider")
    @DisplayName("Update category negative test")
    @Description("Checking that the category update method is not allowed by using API")
    void updateNewCategoryTest(String categoryTitle)  {
        String temp =categoryForTest.getTitle();
        categoryForTest.setTitle(categoryTitle);
        Response response=RetrofitUtils.updateCategoryResponse(categoryForTest.getId(), categoryService);
        step("Check response code 405");
        assertThat(response.code(),is(405));
        step("Check response error is \"Method Not Allowed\"");
        assertThat(ErrorBody.getErrorMessage(response),is(equalTo("Method Not Allowed")));
        Response<Category> responseCategory=RetrofitUtils.getCategoryResponse(categoryForTest.getId(), categoryService);
        step("Check the category wasn't updated");
        assertThat(responseCategory.body().getTitle(),is(equalTo(temp)));
    }
    @SneakyThrows
    @Test
    @DisplayName("Delete category negative test")
    @Description("Checking that the category delete method is not allowed by using API")
    void deleteCategoryTest()  {
        Response response=RetrofitUtils.deleteCategoryResponse(categoryForTest.getId(), categoryService);
        step("Check response code 405");
        assertThat(response.code(),is(405));
        step("Check response error is \"Method Not Allowed\"");
        assertThat(ErrorBody.getErrorMessage(response),is(equalTo("Method Not Allowed")));
        Response<Category> responseCategory=RetrofitUtils.getCategoryResponse(categoryForTest.getId(), categoryService);
        step("Check the category wasn't deleted");
        assertThat(responseCategory.isSuccessful(),is(true));
    }
    @SneakyThrows
    @Test
    @DisplayName("Create category negative test")
    @Description("Checking that the method of adding a category using the API is not implemented")
    void createNewCategoryTest()  {
        Response response=RetrofitUtils.createCategoryResponse
                (new Category(null, categoryForTest.getTitle()
                        ,new ArrayList<>()), categoryService);
        step("Check response code 404");
        assertThat(response.code(),is(404));
        step("Check response error is \"Not Found\"");
        assertThat(ErrorBody.getErrorMessage(response),is(equalTo("Not Found")));
    }
    @AfterAll
    static void afterAll() {
    EnvironmentInfo.setAllureEnvironment();
    }


}
