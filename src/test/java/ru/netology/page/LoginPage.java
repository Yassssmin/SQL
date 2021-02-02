package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement error = $("[data-test-id=error-notification]");

    public VerificationPage login(DataHelper.AuthInfo info){
        $("input.input__control[type='text']").setValue(info.getLogin());
        $("input.input__control[type='password']").setValue(info.getPassword());
        $("[data-test-id=action-login]").click();

        return new VerificationPage();
    }

    public void checkErrorLoginOrPassword() {
        error.shouldBe(text("Ошибка! Неверно указан логин или пароль"));
    }
}
