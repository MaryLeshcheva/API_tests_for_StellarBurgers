import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Order;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserGenerator;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class OrderTest {

    private OrderClient orderClient;
    private String accessToken;
    private User user;
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerator.createDefault();
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Создание заказа авторизированным пользователем")
    public void orderCanBeCreatedByAuthorizedUser() {
        List<String> ingredientsList = Arrays.asList("61c0c5a71d1f82001bdaaa70","61c0c5a71d1f82001bdaaa75");
        Order order = new Order(ingredientsList);
        ValidatableResponse createOrder = orderClient.createAuthorizedUserOrder(accessToken, order);
        int actualStatusCode = createOrder.extract().statusCode();
        boolean isOrderCreated = createOrder.extract().path("success");
        assertEquals("Статус код неверный", SC_OK, actualStatusCode);
        assertTrue("Заказ не создан", isOrderCreated);
    }

    @Test
    @DisplayName("Создание заказа не авторизированным пользователем")
    public void orderCannotBeCreatedByUnauthorizedUser() {
        List<String> ingredientsList = Arrays.asList("61c0c5a71d1f82001bdaaa75","61c0c5a71d1f82001bdaaa70");
        Order order = new Order(ingredientsList);
        ValidatableResponse createOrder = orderClient.createUnauthorizedUserOrder(order);
        int actualStatusCode = createOrder.extract().statusCode();
        boolean isOrderCreated = createOrder.extract().path("success");
        assertEquals("Статус код неверный", SC_OK,actualStatusCode);
        assertTrue("Заказ не создан", isOrderCreated);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void orderCannotBeCreatedWithoutIngredients() {
        ValidatableResponse createOrder = orderClient.createOrderWithoutIngredients(accessToken);
        int actualStatusCode = createOrder.extract().statusCode();
        boolean isOrderCreated = createOrder.extract().path("success");
        String orderMessage = createOrder.extract().path("message");
        assertEquals("Ожидается 400", SC_BAD_REQUEST, actualStatusCode);
        assertFalse("Заказ создан", isOrderCreated);
        assertEquals("Ingredient ids must be provided", orderMessage);
    }

    @Test
    @DisplayName("Создание заказа с неверными ингредиентами") //Тест падает. В документации код 500, по факту 400
    public void orderCannotBeCreatedWithNonExistentIngredients() {
        List<String> nonExistentIngredientsList = Arrays.asList("60d3b41abdacab0026a733c6","609646e4dc916e00276b2870");
        Order order = new Order(nonExistentIngredientsList);
        ValidatableResponse createOrder = orderClient.createAuthorizedUserOrder(accessToken, order);
        int actualStatusCode = createOrder.extract().statusCode();
        assertEquals("Статус код неверный", SC_INTERNAL_SERVER_ERROR, actualStatusCode);
    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    public void getAuthorizedUserOrders() {
        ValidatableResponse getOrders = orderClient.getAuthorizedUserOrders(accessToken, user);
        int actualStatusCode = getOrders.extract().statusCode();
        boolean isOrdersDisplayed = getOrders.extract().path("success");
        assertEquals("Статус код неверный", SC_OK, actualStatusCode);
        assertTrue("Список заказов не получен", isOrdersDisplayed);
    }

    @Test
    @DisplayName("Получение списка заказов не авторизованного пользователя")
    public void getUnauthorizedUserOrders() {
        ValidatableResponse getOrders = orderClient.getUnauthorizedUserOrders(user);
        int actualStatusCode = getOrders.extract().statusCode();
        boolean isOrdersDisplayed = getOrders.extract().path("success");
        String actualMessage = getOrders.extract().path("message");
        assertEquals("Статус код неверный", SC_UNAUTHORIZED, actualStatusCode);
        assertFalse("Список заказов получен", isOrdersDisplayed);
        assertEquals("You should be authorised", actualMessage);
    }
}
