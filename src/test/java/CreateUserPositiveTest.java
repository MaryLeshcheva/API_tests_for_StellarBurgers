import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import user.User;
import user.UserClient;
import user.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateUserPositiveTest {
    private UserClient userClient;
    private User user;
    private String accessToken;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.createDefault();
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Создание пользователя с валидными данными")
    public void userCanBeCreatedAndCheckResponse() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken");
        int actualStatusCode = responseCreate.extract().statusCode();
        boolean isUserCreated = responseCreate.extract().path("success");
        assertEquals("Статус код неверный", SC_OK, actualStatusCode);
        assertTrue("Пользователь не создан", isUserCreated);
    }
}
