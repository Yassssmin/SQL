package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataHelper {
    private DataHelper(){}

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/app", "user", "feada149cb8ff54e"
        );
    }

    private static String getUserId(AuthInfo authInfo) throws SQLException {
        String getUserIdSQL = "SELECT id FROM users WHERE login = ?";
        QueryRunner runner = new QueryRunner();

        return runner.query(getConnection(), getUserIdSQL, new ScalarHandler<>(), authInfo.getLogin());
    }

    @Value
    public static class AuthInfo{
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getAuthInfoInvalid() {
        Faker faker = new Faker();

        return new AuthInfo(faker.name().username(), faker.internet().password());
    }
    public static AuthInfo getAuthInfoVasyaWithInvalidPassword(){
        Faker faker = new Faker();

        return new AuthInfo("vasya", faker.internet().password());
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getInvalidCode() {
        Faker faker = new Faker();

        return new VerificationCode(String.valueOf(faker.random().nextInt(10000, 999999)));
    }

    public static VerificationCode getVerificationCode(AuthInfo authInfo) throws SQLException {
        String userId = getUserId(authInfo);

        String getVerificationCodeSQL = "SELECT code FROM auth_codes WHERE user_id = ?";

        QueryRunner runner = new QueryRunner();

        String code = runner.query(getConnection(), getVerificationCodeSQL, new ScalarHandler<>(), userId);

        return new VerificationCode(code);
    }
    public static void cleanCodes() throws SQLException {
        String cleanVerificationCodeSQL = "DELETE FROM auth_codes";

        QueryRunner runner = new QueryRunner();

        runner.execute(getConnection(), cleanVerificationCodeSQL);
    }

    public static void addCodes(AuthInfo authInfo) throws SQLException {
        Faker faker = new Faker();

        String userId = getUserId(authInfo);
        String code = getInvalidCode().code;

        String addVerificationCodeSQL = "INSERT INTO auth_codes (id, user_id, code) VALUES (?, ?, ?)";

        Object[][] params = new Object[3][3];
        params[0] = new Object[] {
            faker.internet().uuid(), userId,code
        };
        params[1] = new Object[] {
            faker.internet().uuid(), userId, code
        };
        params[2] = new Object[] {
            faker.internet().uuid(), userId, code
        };

        QueryRunner runner = new QueryRunner();
        runner.batch(getConnection(), addVerificationCodeSQL, params);
    }
}
