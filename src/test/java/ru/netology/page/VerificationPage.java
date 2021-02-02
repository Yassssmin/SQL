package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private SelenideElement error = $("[data-test-id=error-notification]");

    public DashboardPage verify(DataHelper.VerificationCode verificationCode) {
        codeField.setValue(verificationCode.getCode());
        verifyButton.click();

        return new DashboardPage();
    }

    public void checkErrorCode() {
        error.shouldBe(text("Ошибка! Неверно указан код! Попробуйте ещё раз."));
    }

    public void checkErrorCodeBlocked() {
        error.waitUntil(visible, 30).shouldBe(text("Ошибка! Превышено колличество попыток ввода кода!"));
    }
}
