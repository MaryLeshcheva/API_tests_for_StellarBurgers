**API тесты для Stellar Burgers. Учебный проект**

API тесты для [Stellar Burgers](https://stellarburgers.nomoreparties.site/). [Документация](https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf) на API приложения.

- Тесты написаны для ручек: создание пользователя, логин пользователя, изменение данных пользователя, создание заказа, получение заказов конкретного пользователя.
- В maven подключены библиотеки: JUnit 4, Allure, rest-assured, gson. Настроен на работу с Java 11.

Команды:
- для запуска тестовых сценариев: mvn clean test 
- для формирования Allure отчёта: mvn allure:serve