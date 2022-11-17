package net.kiwox.dst.script.appium.entel_peru;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

import net.kiwox.dst.script.appium.ITest;
import net.kiwox.dst.script.appium.TestUtils;
import net.kiwox.dst.script.enums.EnumCodeProcessTests;
import net.kiwox.dst.script.pojo.TestResult;
import net.kiwox.dst.script.pojo.TestResultDetailEntelApp;
import net.kiwox.dst.script.pojo.TestResultEntelApp;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static net.kiwox.dst.script.appium.entel_peru.HelperEntelPeruApp.*;

public class EntelPeruAppTestVerifyPostPaidMembershipReceipt implements ITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntelPeruAppTestVerifyPostPaidBalance.class);
    private static final String APP_PACKAGE = "com.entel.movil";
    private static final String APP_ACTIVITY = ".MainActivity";

    private static final String EMAIL_TEST_UPDTE_INFO = "test@kiwox.cl";
    private String phoneNumber;
    private String verificationCode;
    private TestResultEntelApp testResult;
    public static final Map<String, AbstractMap.SimpleEntry<String, String>> STEPS_TEST;

    static {
        STEPS_TEST = new HashMap<String,AbstractMap.SimpleEntry<String,String>>();
    }

    public EntelPeruAppTestVerifyPostPaidMembershipReceipt(String phoneNumber, String verificationCode) {
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.testResult = new TestResultEntelApp();

        STEPS_TEST.put("Step01", new AbstractMap.SimpleEntry<>("DST-ENTEL_PERU-D010-E001", "Acceder a Recibo"));
        STEPS_TEST.put("Step02", new AbstractMap.SimpleEntry<>("DST-ENTEL_PERU-D010-E002", "Ingresar formulario"));
        STEPS_TEST.put("Step03", new AbstractMap.SimpleEntry<>("DST-ENTEL_PERU-D010-E003", "Acceder a envío y confirmación"));
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
        verifyPostPaidMembershipReceipt(driver, contentPath);
        return testResult;
    }

    public void verifyPostPaidMembershipReceipt(AndroidDriver<AndroidElement> driver, String contentPath) throws InterruptedException {
        AbstractMap.SimpleEntry<String, String> errorStep = STEPS_TEST.get("Step01");
        String currentStepErrorCode = errorStep.getKey();
        String currentStepDetail = errorStep.getValue();


        testResult.setCode(EnumCodeProcessTests.VERIFY_POST_PAID_MEMBERSHIP_RECEIPT_SUCCESS_ENTEL_PERU_APP.getCode());
        testResult.setMessage(EnumCodeProcessTests.VERIFY_POST_PAID_MEMBERSHIP_RECEIPT_SUCCESS_ENTEL_PERU_APP.getDescription());
        long totalTimeOutAdd = System.currentTimeMillis(), currentTimeOut = System.currentTimeMillis();
        try {
            WebDriverWait wait = new WebDriverWait(driver, TestUtils.getWaitTimeout());
            String receiptTabPathSelected = contentPath + "//*[@resource-id='b4-b1-b1-BottomBarItems']/*[@class='android.view.View'][3]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(receiptTabPathSelected)))
                    .click();

            Dimension size = driver.manage().window().getSize();
            int y1 = (int) (size.height * 0.70);
            int y2 = (int) (size.height * 0.10);
            int x = size.width / 4;

            Thread.sleep(6000);
            TouchAction ts = new TouchAction(driver);
            ts.press(PointOption.point(x, y1))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                    .moveTo(PointOption.point(x, y2))
                    .release()
                    .perform();

            String cardFreeMembershipDigitalReceiptPathSelected = contentPath + "//*[@resource-id='b1-b15-Action']/*[@class='android.view.View'][3]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(cardFreeMembershipDigitalReceiptPathSelected)))
                    .click();

            Thread.sleep(1000);
            String editDataButtonFreeMembershipDigitalReceiptPathSelected = contentPath + "//*[@resource-id='b1-Content_Success']/*[@class='android.view.View'][1]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(editDataButtonFreeMembershipDigitalReceiptPathSelected)))
                    .click();

            TestUtils.saveScreenshot(driver);
            testResult.addItemDetail((new TestResultDetailEntelApp().setCode(errorStep.getKey())
                        .setDescription(currentStepDetail)
                        .setDetail(currentStepDetail)
                        .setErrorDetected(false)
                        .setTime(System.currentTimeMillis() - currentTimeOut)));
            //Step2
            currentTimeOut = System.currentTimeMillis();
            errorStep = STEPS_TEST.get("Step02");
            currentStepErrorCode = errorStep.getKey(); 
            currentStepDetail = errorStep.getValue();

    
            String emailUpdatePersonaDataTextPath = contentPath + "//*[@resource-id='b1-b1-MainContent']//*[@resource-id='b1-b4-Input']";
            String confirmEmailUpdatePersonaDataTextPath = contentPath + "//*[@resource-id='b1-b1-MainContent']//*[@resource-id='b1-b5-Input']";
            String buttonMembershipPath = contentPath + "//*[@resource-id='b1-b1-MainContent']//*[@class='android.widget.Button' and @text='AFILIARME']";


            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(emailUpdatePersonaDataTextPath)))
                    .sendKeys(EMAIL_TEST_UPDTE_INFO);
            

            AndroidElement emailEdit = driver.findElementsByXPath(emailUpdatePersonaDataTextPath).get(0);
            Point emailPointPath = emailEdit.getCenter();
            emailPointPath.move(0, emailPointPath.getY());
            new TouchAction<>(driver).press(new PointOption<>().withCoordinates(emailPointPath))
                    .perform();


            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(confirmEmailUpdatePersonaDataTextPath)))
                    .sendKeys(EMAIL_TEST_UPDTE_INFO);
            

            AndroidElement confirmEmailPath = driver.findElementsByXPath(confirmEmailUpdatePersonaDataTextPath).get(0);
            Point confirmEmailPoint = confirmEmailPath.getCenter();
            confirmEmailPoint.move(0, confirmEmailPoint.getY());
            new TouchAction<>(driver).press(new PointOption<>().withCoordinates(confirmEmailPoint))
                    .perform();


            Thread.sleep(1000);

            TestUtils.saveScreenshot(driver);
            testResult.addItemDetail((new TestResultDetailEntelApp().setCode(errorStep.getKey())
                        .setDescription(currentStepDetail)
                        .setDetail(currentStepDetail)
                        .setErrorDetected(false)
                        .setTime(System.currentTimeMillis() - currentTimeOut)));
            //Step3
            currentTimeOut = System.currentTimeMillis();
            errorStep = STEPS_TEST.get("Step03");
            currentStepErrorCode = errorStep.getKey(); 
            currentStepDetail = errorStep.getValue();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(buttonMembershipPath)))
                    .click();

            //Thread.sleep(1000);

            String verifyEmailRegisteredPath = contentPath + String.format("//*[@resource-id='b5-Content_Success']//*[@class='android.widget.TextView' and @text='%s']", EMAIL_TEST_UPDTE_INFO);
            String verifyEmailRegisteredTextLabel = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(verifyEmailRegisteredPath)))
                    .getText();

            boolean expectedTextFound = verifyEmailRegisteredTextLabel.trim()
                    .equals(EMAIL_TEST_UPDTE_INFO);
            String finalMessage = String.format("Correo electrónico: %s, %s fue registrado.", EMAIL_TEST_UPDTE_INFO, (expectedTextFound ? "si" : "no"));
            testResult.setError(!expectedTextFound);
            if (testResult.isError()) {
                testResult.setErrorMessage(finalMessage);
                testResult.setCode(EnumCodeProcessTests.VERIFY_POST_PAID_MEMBERSHIP_RECEIPT_ERROR_ENTEL_PERU_APP.getCode());
            }

            TestUtils.saveScreenshot(driver);
            testResult.addItemDetail((new TestResultDetailEntelApp().setCode(errorStep.getKey())
                    .setDescription(currentStepDetail)
                    .setDetail(currentStepDetail)
                    .setErrorDetected(false)
                    .setTime(System.currentTimeMillis() - currentTimeOut)));
        } catch (Exception e) {
            testResult.addItemDetail(addErrorDetailItem(
                    currentStepErrorCode,
                    currentStepDetail,
                    e.getMessage(),
                    currentTimeOut)
            );

            testResult.setError(true);
            testResult.setCode(EnumCodeProcessTests.VERIFY_POST_PAID_MEMBERSHIP_RECEIPT_ERROR_ENTEL_PERU_APP.getCode());
            testResult.setMessage(EnumCodeProcessTests.VERIFY_POST_PAID_MEMBERSHIP_RECEIPT_ERROR_ENTEL_PERU_APP.getDescription());
            testResult.setErrorMessage(e.getMessage());
            TestUtils.saveScreenshot(driver);
        } finally {
            if (testResult.isError()) {
                testResult.setMessage(EnumCodeProcessTests.VERIFY_POST_PAID_MEMBERSHIP_RECEIPT_ERROR_ENTEL_PERU_APP.getDescription());
            }
            testResult.addTime(System.currentTimeMillis() - totalTimeOutAdd);
            closeApp(driver);
        }
    }
}
