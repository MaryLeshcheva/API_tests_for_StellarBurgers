import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserClient;
import org.example.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginUserPositiveTest {
    private UserClient userClient;
    private User user;
    private String accessToken;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.createDefault();
        ValidatableResponse responseCreate = userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Авторизация пользователя с валидными данными")
    public void userCanBeLoginAndCheckResponse() {
        ValidatableResponse responseLogin = userClient.login(user);
        int actualStatusCode = responseLogin.extract().statusCode();
        boolean isUserLoggedIn = responseLogin.extract().path("success");
        assertEquals("Статус код неверный", actualStatusCode, SC_OK);
        assertTrue("Пользователь не авторизирован", isUserLoggedIn);
    }
}
