package net.kiwox.dst.script.appium.entel_peru;

import static net.kiwox.dst.script.appium.entel_peru.HelperEntelPeruApp.addErrorDetailItem;
import static net.kiwox.dst.script.appium.entel_peru.HelperEntelPeruApp.closeApp;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import net.kiwox.dst.script.appium.ITest;
import net.kiwox.dst.script.appium.TestUtils;
import net.kiwox.dst.script.enums.EnumCodeProcessTests;
import net.kiwox.dst.script.pojo.TestResult;
import net.kiwox.dst.script.pojo.TestResultDetailEntelApp;
import net.kiwox.dst.script.pojo.TestResultEntelApp;

public class EntelPeruAppTestVerifyPostPaidAdditionalLine implements ITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntelPeruAppTestVerifyPostPaidAdditionalLine.class);
    private static final String APP_PACKAGE = "com.entel.movil";
    private static final String APP_ACTIVITY = ".MainActivity";

    private String phoneNumber;
    private String verificationCode;
    private TestResultEntelApp testResult;

    public static final Map<String, AbstractMap.SimpleEntry<String, String>> STEPS_TEST;

    static {
        STEPS_TEST = Collections.singletonMap("Step01", new AbstractMap.SimpleEntry<>("DST-ENTEL_PERU-D007-E001", "Acceder a Línea adicional"));
    }


    public EntelPeruAppTestVerifyPostPaidAdditionalLine(String phoneNumber, String verificationCode) {
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
        verifyPostPaidAdditionalLine(driver, contentPath);
        return testResult;
    }


    public void verifyPostPaidAdditionalLine(AndroidDriver<AndroidElement> driver, String contentPath) throws InterruptedException {
        AbstractMap.SimpleEntry<String, String> errorStep = STEPS_TEST.get("Step01");
        String currentStepErrorCode = errorStep.getKey(), currentStepDetail = errorStep.getValue();

        testResult.setCode(EnumCodeProcessTests.VERIFY_POST_PAID_ADDITIONAL_LINE_SUCCESS_ENTEL_PERU_APP.getCode());
        testResult.setMessage(EnumCodeProcessTests.VERIFY_POST_PAID_ADDITIONAL_LINE_SUCCESS_ENTEL_PERU_APP.getDescription());
        long totalTimeOutAdd = System.currentTimeMillis(), currentTimeOut = System.currentTimeMillis();
        try {
            WebDriverWait wait = new WebDriverWait(driver, TestUtils.getWaitTimeout());
            
            String carouselPath = contentPath + "//*[@resource-id='b1-b1-MainContent']//*[@resource-id='b1-PromotionMain']//*[@resource-id='b1-b6-carousel']";
            String goToFirstPath = carouselPath + "//*[@class='android.widget.Button' and @text='Go To Item 1']";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(goToFirstPath))).click();
            String firstImgPath = carouselPath + "//*[@class='android.widget.Image']";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(firstImgPath))).click();

            boolean expectedTextFound = waitForAdditionalLineTexts(driver, wait, contentPath);
            
            currentTimeOut = System.currentTimeMillis();
            
            testResult.setError(!expectedTextFound);
            if (testResult.isError()){
                testResult.setErrorMessage("Texto \"Obtenlo hoy\" no fue encontrado en la pantalla seleccionada");
                testResult.setCode(EnumCodeProcessTests.VERIFY_RECEIPT_ERROR_ENTEL_PERU_APP.getCode());
            }
            TestUtils.saveScreenshot(driver);

            testResult.addItemDetail((new TestResultDetailEntelApp().setCode("DST-ENTEL_PERU-D007-001")
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
            testResult.setCode(EnumCodeProcessTests.VERIFY_POST_PAID_ADDITIONAL_LINE_ERROR_ENTEL_PERU_APP.getCode());
            testResult.setMessage(EnumCodeProcessTests.VERIFY_POST_PAID_ADDITIONAL_LINE_ERROR_ENTEL_PERU_APP.getDescription());
            testResult.setErrorMessage(e.getMessage());

            TestUtils.saveScreenshot(driver);
        } finally {
            if (testResult.isError()){
                testResult.setMessage(EnumCodeProcessTests.VERIFY_POST_PAID_ADDITIONAL_LINE_ERROR_ENTEL_PERU_APP.getDescription());
            }
            testResult.addTime(System.currentTimeMillis() - totalTimeOutAdd);
            closeApp(driver);
        }
    }
    
    private boolean waitForAdditionalLineTexts(AndroidDriver<AndroidElement> driver, WebDriverWait wait, String contentPath) {
    	// Wait for a WebView
    	String webviewPath = contentPath + "//*[@class='android.webkit.WebView' and @text='Additional_Postpaid']";
    	try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(webviewPath)));
		} catch (TimeoutException e) {
			return false;
		}
    	
    	// Check text inmediately
    	String textPath = webviewPath + "//*[@resource-id='b1-b1-MainContent']/*[@class='android.widget.TextView' and (@text='Obtenlo hoy' or @text='Obtenlo hoy por nuestros canales')]";
    	List<AndroidElement> elements = driver.findElementsByXPath(textPath);
    	if (!elements.isEmpty()) {
    		return true;
    	}
    	
    	// If not found, press device BACK button to refresh the webview and try again
    	driver.pressKey(new KeyEvent(AndroidKey.BACK));
    	try {
    		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(textPath)));
    		return true;
		} catch (TimeoutException e) {
	    	return false;
		}
    }
}
