package praktikum.tests;

import io.restassured.response.Response;
import org.apache.http.auth.Credentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.model.User;
import praktikum.model.UserCredentials;
import praktikum.utils.UserGenerator;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();

        Response createResponse = userClient.create(user);
        accessToken = createResponse.path("accessTocen");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    //Вход под существующим пользователем
    @Test
    public void loginExistingUserReturnSuccess() {
        UserCredentials credentials = new UserCredentials(user.getEmail(), user.getPassword());

        Response response = userClient.login(credentials);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    //Вход с неверным логином и паролем
    @Test
    public void loginWithIncorrectCredentialsReturnsError() {
        UserCredentials incorrectCredentials = new UserCredentials("wrongEmail@mail.ru", "wrongPassword");

        Response response = userClient.login(incorrectCredentials);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
