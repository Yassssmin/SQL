package ru.netology.test;

import com.github.javafaker.Code;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestVerificationUsers {
    public DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        open("http://0.0.0.0:9999");
    }

    @Test
    void shouldBeLogin() throws SQLException {
        DataHelper.cleanCodes();
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.login(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        dashboardPage = verificationPage.verify(verificationCode);
    }

    @Test
    void shouldNotLoginWithInvalidVerification() throws SQLException {
        DataHelper.cleanCodes();
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.login(authInfo);
        val verificationCodeInvalid = DataHelper.getInvalidCode();
        verificationPage.verify(verificationCodeInvalid);
        verificationPage.checkErrorCode();
    }

    @Test
    void shouldNotLoginWithInvalidUser() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfoInvalid();
        loginPage.login(authInfo);
        loginPage.checkErrorLoginOrPassword();
    }

    @Test
    void shouldNotLoginWithInvalidPassword() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfoVasyaWithInvalidPassword();
        loginPage.login(authInfo);
        loginPage.checkErrorLoginOrPassword();
    }

    @Test
    void shouldNotLoginWithInvalidVerificationBlocked() throws SQLException {
        DataHelper.cleanCodes();
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        DataHelper.addCodes(authInfo);
        val verificationPage = loginPage.login(authInfo);
        val verificationCodeInvalid = DataHelper.getInvalidCode();
        verificationPage.verify(verificationCodeInvalid);
        verificationPage.checkErrorCodeBlocked();
    }
}
