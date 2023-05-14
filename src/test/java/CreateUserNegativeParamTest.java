import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserClient;
import org.example.UserGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class CreateUserNegativeParamTest {

    private UserClient userClient;
    private final User user;
    private final int statusCode;
    private final String message;

    public CreateUserNegativeParamTest(User user, int statusCode, String message) {
        this.user = user;
        this.statusCode = statusCode;
        this.message = message;
    }
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {UserGenerator.createWithoutEmail(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserGenerator.createWithoutPassword(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserGenerator.createWithoutName(), SC_FORBIDDEN, "Email, password and name are required fields"}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Создание пользователя с незаполненным обязательным полем")
    public void createUserWithOutOneParameterCheckStatusCode() {
        ValidatableResponse responseCreate = userClient.create(user);
        int actualStatusCode = responseCreate.extract().statusCode();
        boolean isUserCreated = responseCreate.extract().path("success");
        String actualMessage = responseCreate.extract().path("message" );
        assertEquals("Статус код неверный", statusCode, actualStatusCode);
        assertFalse("Пользователь создан", isUserCreated);
        assertEquals("Сообщение об ошибке неверное", message, actualMessage);
    }
}
