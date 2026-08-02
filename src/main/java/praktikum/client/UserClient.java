package praktikum.client;

import io.restassured.response.Response;
import praktikum.model.User;
import praktikum.model.UserCredentials;
import static io.restassured.RestAssured.given;
import static praktikum.config.Config.BASE_URL;
import io.qameta.allure.Step;

public class UserClient {
    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String USER_PATH = "/api/auth/user";

    @Step("Создать пользователя")
    public Response create(User user) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(REGISTER_PATH);
    }

    @Step("Аторизовать пользователя")
    public Response login(UserCredentials credentials) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удалить пользователя")
    public Response delete(String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .delete(USER_PATH);
    }
}
