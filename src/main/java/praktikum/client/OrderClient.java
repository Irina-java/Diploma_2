package praktikum.client;

import io.restassured.response.Response;
import praktikum.model.Order;
import static io.restassured.RestAssured.given;
import static praktikum.config.Config.BASE_URL;
import io.qameta.allure.Step;

public class OrderClient {
    private static final String ORDERS_PATH = "/api/orders";

    @Step("Создать заказ без авторизации")
    public Response create(Order order) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Создать заказ с авторизацией")
    public Response createWithAuthorization(Order order, String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }
}
