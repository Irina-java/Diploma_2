package praktikum.tests;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.model.User;
import praktikum.model.UserCredentials;
import praktikum.utils.UserGenerator;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import static org.apache.http.HttpStatus.*;

public class UserLoginTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();

        Response createResponse = userClient.create(user);
        accessToken = createResponse.path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    //Вход под существующим пользователем
    @Test
    @DisplayName("Авторизация существующего пользователя")
    @Description("Проверка успешной авторизации пользователя с валидными даннми")
    public void loginExistingUserReturnSuccess() {
        UserCredentials credentials = new UserCredentials(user.getEmail(), user.getPassword());

        Response response = userClient.login(credentials);

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    //Вход с неверным логином
    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверка невозможности авторизации пользователя при передачи неверного email")
    public void loginWithIncorrectEmailReturnsError() {
        UserCredentials incorrectCredentials = new UserCredentials("wrongEmail@mail.ru", user.getPassword());

        Response response = userClient.login(incorrectCredentials);

        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));

    }

    //Вход с неверным логином
    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка невозможности авторизации пользователя при передачи неверного password")
    public void loginWithIncorrectPasswordReturnsError() {
        UserCredentials incorrectCredentials = new UserCredentials(user.getEmail(), "wrongPassword");

        Response response = userClient.login(incorrectCredentials);

        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));

    }

}
