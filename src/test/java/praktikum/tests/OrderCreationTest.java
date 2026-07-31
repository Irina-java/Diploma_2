package praktikum.tests;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.OrderClient;
import praktikum.client.UserClient;
import praktikum.model.Order;
import praktikum.model.User;
import praktikum.utils.UserGenerator;

import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreationTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    //Создание заказа без авторизации с ингредиентами
    @Test
    public void createOrderWithoutAuthorizationWithIngredientsReturnsSuccess() {
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"));

        Response response = orderClient.create(order);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    //Создание заказа с авторизацией и с ингредиентами
    @Test
    public void createOrderWithoutAuthorizationReturnsSuccess() {
        UserClient userClient = new UserClient();

         User user = UserGenerator.getRandomUser();
         Response createUserResponse = userClient.create(user);
         String accessToken = createUserResponse.path("accessToken");

         Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"));

         Response response = orderClient.createWithAuthorization(order, accessToken);

         response.then()
                 .statusCode(200)
                 .body("success", equalTo(true))
                 .body("order.number", notNullValue());

         userClient.delete(accessToken);
    }

    //Создание заказа без ингредиентов
    @Test
    public void createOrderWithoutIngredientsReturnsError() {
        Order order = new Order(List.of());

        Response response = orderClient.create(order);

        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    //Создание заказа с неверным хешем
    @Test
    public void createOrderWithIncorrectIngredientHashReturnsError() {
        Order order = new Order(List.of("incorrectIngredientHash"));

        Response response = orderClient.create(order);
        response.then()
                .statusCode(500);

    }
}
