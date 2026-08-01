package praktikum.tests;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.model.User;
import praktikum.utils.UserGenerator;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

public class UserCreationTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    //Создание уникального пользователя
    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания пользователя с валидными данными")
    public void createUniqueUserReturnsSuccess() {
        Response response = userClient.create(user);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        accessToken = response.path("accessToken");
    }

    //Создания пользователя который уже зарегестрировался
    @Test
    @DisplayName("Создание уже существующего пользователя")
    @Description("Проверка невозможности повторного создания пользователя с уже зарегестрированными данными")
    public void createExistingUserReturnError() {
        Response firstResponse = userClient.create(user);
        accessToken = firstResponse.path("accessToken");

        Response secondResponse = userClient.create((user));

        secondResponse.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    //Создание пользователя без обязательного поля "email"
    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка невозможности создания пользователя без обязательного поля email")
    public void createUserWithoutEmailReturnsError() {
        User userWithoutEmail = new User(null, user.getPassword(), user.getName());

        Response response = userClient.create(userWithoutEmail);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    //Создание пользователя без обязательного поля "password"
    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка невозможности создания пользователя без обязательного поля password")
    public void createUserWithoutPasswordReturnsError() {
        User userWithoutPassword = new User(user.getEmail(), null, user.getName());

        Response response = userClient.create(userWithoutPassword);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    //Создание пользователя без обязательного поля "name"
    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка невозможности создания пользователя без обязательного поля name")
    public void createUserWithoutNameReturnsError() {
        User userWithoutName = new User(user.getEmail(), user.getPassword(), null);

        Response response = userClient.create(userWithoutName);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
