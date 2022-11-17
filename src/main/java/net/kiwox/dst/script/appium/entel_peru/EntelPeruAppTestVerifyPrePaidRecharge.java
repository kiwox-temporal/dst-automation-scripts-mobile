package net.kiwox.dst.script.appium.entel_peru;

import static net.kiwox.dst.script.appium.entel_peru.HelperEntelPeruApp.addErrorDetailItem;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class EntelPeruAppTestVerifyPrePaidRecharge implements ITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntelPeruAppTestVerifyPrePaidRecharge.class);
    private static final String APP_PACKAGE = "com.entel.movil";
    private static final String APP_ACTIVITY = ".MainActivity";

    private String phoneNumber;
    private String verificationCode;

    private TestResultEntelApp testResult;
    public static final Map<String, AbstractMap.SimpleEntry<String, String>> STEPS_TEST;

    static {
        STEPS_TEST = new HashMap<String, AbstractMap.SimpleEntry<String, String>>();
        STEPS_TEST.put("Step01", new AbstractMap.SimpleEntry<>("DST-ENTEL_PERU-D005-E001", "Acceder a Recargas"));
        STEPS_TEST.put("Step02", new AbstractMap.SimpleEntry<>("DST-ENTEL_PERU-D005-E002", "Buscar botón Recargar"));
        STEPS_TEST.put("Step03", new AbstractMap.SimpleEntry<>("DST-ENTEL_PERU-D005-E003", "Acceder a Recargar"));
    }

    public EntelPeruAppTestVerifyPrePaidRecharge(String phoneNumber, String verificationCode) {
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
        verifyPrePaidRecharge(driver, contentPath);
        return testResult;
    }

    public void verifyPrePaidRecharge(AndroidDriver<AndroidElement> driver, String contentPath) throws InterruptedException {
        AbstractMap.SimpleEntry<String, String> errorStep = STEPS_TEST.get("Step01");
        String currentStepErrorCode = errorStep.getKey(), currentStepDetail = errorStep.getValue();

        testResult.setCode(EnumCodeProcessTests.VERIFY_PRE_PAID_RECHARGE_SUCCESS_ENTEL_PERU_APP.getCode());
        testResult.setMessage(EnumCodeProcessTests.VERIFY_PRE_PAID_RECHARGE_SUCCESS_ENTEL_PERU_APP.getDescription());
        long totalTimeOutAdd = System.currentTimeMillis(), currentTimeOut = System.currentTimeMillis();

        try {
            WebDriverWait wait = new WebDriverWait(driver, TestUtils.getWaitTimeout());
            String receiptOptionPath = contentPath + "//*[@resource-id='b3-b1-b1-BottomBarItems']/*[@class='android.view.View'][3]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(receiptOptionPath)))
                    .click();

            TestUtils.saveScreenshot(driver);
            testResult.addItemDetail((new TestResultDetailEntelApp().setCode(errorStep.getKey())
                    .setDescription(currentStepDetail)
                    .setDetail(currentStepDetail)
                    .setErrorDetected(false)
                    .setTime(System.currentTimeMillis() - currentTimeOut)));


            //Stepp 2
            currentTimeOut = System.currentTimeMillis();
            errorStep = STEPS_TEST.get("Step02");
            currentStepErrorCode = errorStep.getKey();
            currentStepDetail = errorStep.getValue();
            
            String numberLabelPath = contentPath + "//*[@resource-id='b1-Titulo1']";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(numberLabelPath)));
            scrollDown(driver, contentPath, wait);

            String contentTermsPath = contentPath + "//*[@resource-id='b1-ContentTerms']//*[@class='android.widget.CheckBox']";
            driver.findElementByXPath(contentTermsPath).click();

            TestUtils.saveScreenshot(driver);
            testResult.addItemDetail((new TestResultDetailEntelApp().setCode(errorStep.getKey())
                    .setDescription(currentStepDetail)
                    .setDetail(currentStepDetail)
                    .setErrorDetected(false)
                    .setTime(System.currentTimeMillis() - currentTimeOut)));

            //Stepp 3
            currentTimeOut = System.currentTimeMillis();
            errorStep = STEPS_TEST.get("Step03");
            currentStepErrorCode = errorStep.getKey();
            currentStepDetail = errorStep.getValue();

            String rechargeButton = contentPath + "//*[@class='android.view.View' and ./*[@text='RECARGAR']]/*[@class='android.widget.Button'][1]";

            /*size = driver.manage().window().getSize();
            int yButton = (int) (size.height * 0.80);
            int xButton = size.width / 2;
            //Thread.sleep(6000);
            ts.tap(new PointOption<>().withCoordinates(new Point(xButton, yButton)))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                    //.release()
                    .perform();*/
            driver.findElementByXPath(rechargeButton).click();

            boolean expectedTextFound = waitForOperationNumber(contentPath, wait);
            testResult.setError(!expectedTextFound);
            if (testResult.isError()) {
                testResult.setCode(EnumCodeProcessTests.VERIFY_PRE_PAID_RECHARGE_ERROR_ENTEL_PERU_APP.getCode());
                testResult.setErrorMessage("Texto \"Orden Nro: XXX-XXX-XX\" no fue encontrado luego de presionar el boton RECARGAR.");
            }

            TestUtils.saveScreenshot(driver);
            testResult.addItemDetail((new TestResultDetailEntelApp().setCode(errorStep.getKey())
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
            testResult.setCode(EnumCodeProcessTests.VERIFY_PRE_PAID_RECHARGE_ERROR_ENTEL_PERU_APP.getCode());
            testResult.setMessage(EnumCodeProcessTests.VERIFY_PRE_PAID_RECHARGE_ERROR_ENTEL_PERU_APP.getDescription());
            testResult.setErrorMessage(e.getMessage());

            TestUtils.saveScreenshot(driver);
        } finally {
            if (testResult.isError()) {
                testResult.setMessage(EnumCodeProcessTests.VERIFY_PRE_PAID_RECHARGE_ERROR_ENTEL_PERU_APP.getDescription());
            }
            testResult.addTime(System.currentTimeMillis() - totalTimeOutAdd);
            //closeApp(driver);
        }
    }
    
    private void scrollDown(AndroidDriver<AndroidElement> driver, String contentPath, WebDriverWait wait) {
        Dimension size = driver.manage().window().getSize();
        int y1 = (int) (size.height * 0.90);
        int y2 = (int) (size.height * 0.10);
        String rechargePath = contentPath + "//*[@class='android.view.View' and ./*[@text='RECARGAR']]/*[@class='android.widget.Button'][1]";
        WebDriverWait smallWait = new WebDriverWait(driver, 1);
        
        // Keep scrolling until the recharge button appears
        for (int i = 0; i < 5; ++i) {
            new TouchAction<>(driver)
            	.press(PointOption.point(0, y1))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(0, y2))
                .release()
                .perform();
            try {
				smallWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(rechargePath)));
				return;
			} catch (TimeoutException e) {
				// Keep trying
			}
        }
    }
    
    private boolean waitForOperationNumber(String contentPath, WebDriverWait wait) {
        String operationNumberPath = contentPath + "//*[@resource-id='com.entel.movil:id/headerOperationNumber']";
        try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(operationNumberPath))).getText();
			return true;
		} catch (TimeoutException e) {
	        return false;
		}
    }
}
