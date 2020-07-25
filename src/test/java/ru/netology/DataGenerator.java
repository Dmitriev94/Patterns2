package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;


public class DataGenerator {
    private DataGenerator() {
    }

    public static void setUpUser(UserData user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .addFilter(new ResponseLoggingFilter())
            .log(LogDetail.ALL)
            .build();

    public static class RegistrationInfo {
        private RegistrationInfo() {
        }

        public static String userPassword(String Locale) {
            Faker faker = new Faker(new Locale(Locale));
            return faker.internet().password();
        }

        public static String userName(String Locale) {
            Faker faker = new Faker(new Locale(Locale));
            return faker.name().username();
        }

        public static UserData generateUserInfo(String locale, boolean isBlocked) {
            return new UserData(
                    userName(locale),
                    userPassword(locale),
                    (isBlocked) ? "blocked" : "active");

        }

        public static UserData generateValidUserInfo(String locale, boolean isBlocked) {
            UserData user = generateUserInfo(locale, isBlocked);
            setUpUser(user);
            return user;
        }

        public static UserData generateInvalidLoginUserInfo(String locale, boolean isBlocked) {
            String password = userPassword(locale);
            UserData user = new UserData(
                    "vasya",
                    password,
                    (isBlocked) ? "blocked" : "active");
            setUpUser(user);
            return new UserData(
                    "petya",
                    password,
                    (isBlocked) ? "blocked" : "active");
        }

        public static UserData generateInvalidPasswordUserInfo(String locale, boolean isBlocked) {
            String login = userName(locale);
            UserData user = new UserData(
                    login,
                    "validpass",
                    (isBlocked) ? "blocked" : "active");
            setUpUser(user);
            return new UserData(
                    login,
                    "invalidpass",
                    (isBlocked) ? "blocked" : "active");
        }
    }
}