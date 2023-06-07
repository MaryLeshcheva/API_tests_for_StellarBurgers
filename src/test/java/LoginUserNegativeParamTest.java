import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import user.User;
import user.UserClient;
import user.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class LoginUserNegativeParamTest {
    private static final User registeredUser = UserGenerator.createDefault();

    private UserClient userClient;
    private final User user;
    private final int statusCode;
    private final String message;
    private String accessToken;

    public LoginUserNegativeParamTest(User user, int statusCode, String message) {
        this.user = user;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] getTestLoginData() {
        return new Object[][] {
                {new User(null, registeredUser.getPassword(), registeredUser.getName()), SC_UNAUTHORIZED, "email or password are incorrect"},
                {new User(registeredUser.getEmail(), null, registeredUser.getName()), SC_UNAUTHORIZED, "email or password are incorrect"},
                {new User(null, null, registeredUser.getName()), SC_UNAUTHORIZED, "email or password are incorrect"},
                {new User("invalid_email", registeredUser.getPassword(), registeredUser.getName()), SC_UNAUTHORIZED, "email or password are incorrect"},
                {new User("wrong_email@icloud.com", registeredUser.getPassword(), registeredUser.getName()), SC_UNAUTHORIZED, "email or password are incorrect"},
                {new User(registeredUser.getEmail(), "wrong_password", registeredUser.getName()), SC_UNAUTHORIZED, "email or password are incorrect"},
                {new User("wrong_email@icloud.com", "wrong_password", registeredUser.getName()), SC_UNAUTHORIZED, "email or password are incorrect"}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        ValidatableResponse responseCreate = userClient.createUser(registeredUser);
        accessToken = responseCreate.extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Авторизация с неправильными учетными данными")
    public void userCantLoginWithoutOneParameterCheckStatusCode() {
        ValidatableResponse responseLogin = userClient.login(user);
        int actualStatusCode = responseLogin.extract().statusCode();
        boolean isUserLoggedIn = responseLogin.extract().path("success");
        String actualMessage = responseLogin.extract().path("message");
        assertEquals("Статус код неверный", statusCode, actualStatusCode);
        assertFalse("Пользователь авторизован", isUserLoggedIn);
        assertEquals("Сообщение неверное", message, actualMessage);
    }
}
