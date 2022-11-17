package net.kiwox.dst.script.appium.entel_peru;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import net.kiwox.dst.script.appium.ITest;
import net.kiwox.dst.script.appium.TestUtils;
import net.kiwox.dst.script.enums.EnumCodeProcessTests;
import net.kiwox.dst.script.pojo.TestResult;
import net.kiwox.dst.script.pojo.TestResultDetailEntelApp;
import net.kiwox.dst.script.pojo.TestResultEntelApp;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;

import static net.kiwox.dst.script.appium.entel_peru.HelperEntelPeruApp.*;

public class EntelPeruAppTestVerifyPostPaidBag implements ITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntelPeruAppTestVerifyPostPaidBalance.class);
    private static final String APP_PACKAGE = "com.entel.movil";
    private static final String APP_ACTIVITY = ".MainActivity";

    private static final int EDIT_BUTTONS_TIMEOUT = 10;

    private static final int UPLOAD_TIMEOUT = 300;

    private String phoneNumber;
    private String verificationCode;
    private TestResultEntelApp testResult;
    public static final Map<String, AbstractMap.SimpleEntry<String, String>> STEPS_TEST;
    static {
        STEPS_TEST = Collections.singletonMap("Step01", new AbstractMap.SimpleEntry<>("DST-ENTEL_PERU-D002-E001", "Acceder a Bolsas Pospago"));
    }

    public EntelPeruAppTestVerifyPostPaidBag(String phoneNumber, String verificationCode) {
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.testResult = new TestResultEntelApp();
    }

    @Override
    public TestResult runTest(AndroidDriver<AndroidElement> driver) throws InterruptedException {
        // TODO: separte startup tests configurations
        // Start activity
        Activity activity = new Activity(APP_PACKAGE, APP_ACTIVITY);
        activity.setAppWaitPackage(APP_PACKAGE);
        activity.setAppWaitActivity("com.entel.*");
        driver.startActivity(activity);
        //WebDriverWait wait = new WebDriverWait(driver, TestUtils.getWaitTimeout());

        // Wait for an expected activity
        String contentPath = "//*[@resource-id='android:id/content']";
        testResult = new EntelPeruAppTestSignIn(phoneNumber, verificationCode)
                .automateStepsSignIn(driver, contentPath);
        //closePromotionPopUp(driver);
        verifyPaymentBag(driver, contentPath);
        return testResult;
    }

    public void verifyPaymentBag(AndroidDriver<AndroidElement> driver, String contentPath) throws InterruptedException {
        AbstractMap.SimpleEntry<String, String> errorStep = STEPS_TEST.get("Step01");
        String currentStepErrorCode = errorStep.getKey(), currentStepDetail = errorStep.getValue();

        testResult.setCode(EnumCodeProcessTests.VERIFY_POST_PAID_BAG_SUCCESS_ENTEL_PERU_APP.getCode());
        testResult.setMessage(EnumCodeProcessTests.VERIFY_POST_PAID_BAG_SUCCESS_ENTEL_PERU_APP.getDescription());
        long totalTimeOutAdd = System.currentTimeMillis(), currentTimeOut = System.currentTimeMillis();
        try {
            WebDriverWait wait = new WebDriverWait(driver, TestUtils.getWaitTimeout());
            String postPaidOptionPath = contentPath + "//*[@resource-id='b4-b1-b1-BottomBarItems']/*[@class='android.view.View'][4]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(postPaidOptionPath)))
                    .click();

            String selectBagOfBuyTextPath = contentPath + "//*[@resource-id='b1-b1-Tus_Ultimas_Compras']//*[@class='android.widget.TextView'][1]";
            String selectBagOfBuyTextLabel = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(selectBagOfBuyTextPath)))
                    .getText();
            boolean expectedTextFound = selectBagOfBuyTextLabel.trim()
                    .equals("Selecciona la bolsa que desees comprar");
            String finalMessage = String.format("Texto \"Selecciona la bolsa que desees comprar\" %s fue encontrado en el TAB seleccionado", (expectedTextFound ? "si" : "no"));
            testResult.setError(!expectedTextFound);
            if (testResult.isError()){
                testResult.setErrorMessage(finalMessage);
                testResult.setCode(EnumCodeProcessTests.VERIFY_RECEIPT_ERROR_ENTEL_PERU_APP.getCode());
            }
            TestUtils.saveScreenshot(driver);

            testResult.addItemDetail((new TestResultDetailEntelApp().setCode("DST-ENTEL_PERU-D002-001")
                    .setDescription(currentStepDetail)
                    .setDetail(currentStepDetail)
                    .setErrorDetected(!expectedTextFound)
                    .setTime(System.currentTimeMillis() - currentTimeOut)));
        } catch (Exception e) {

            testResult.addItemDetail(addErrorDetailItem(
                    currentStepErrorCode,
                    currentStepDetail,
                    e.getMessage(),
                    currentTimeOut)
            );
            testResult.setError(true);
            testResult.setCode(EnumCodeProcessTests.VERIFY_POST_PAID_BALANCE_ERROR_ENTEL_PERU_APP.getCode());
            testResult.setMessage(EnumCodeProcessTests.VERIFY_POST_PAID_BALANCE_ERROR_ENTEL_PERU_APP.getDescription());
            testResult.setErrorMessage(e.getMessage());

            TestUtils.saveScreenshot(driver);
        } finally {

            testResult.addTime(System.currentTimeMillis() - totalTimeOutAdd);
            closeApp(driver);
        }
    }
}
