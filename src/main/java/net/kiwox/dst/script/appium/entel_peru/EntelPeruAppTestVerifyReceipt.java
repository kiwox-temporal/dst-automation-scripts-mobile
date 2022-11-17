package net.kiwox.dst.script.appium.entel_peru;

import static net.kiwox.dst.script.appium.entel_peru.HelperEntelPeruApp.addErrorDetailItem;
import static net.kiwox.dst.script.appium.entel_peru.HelperEntelPeruApp.closeApp;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import net.kiwox.dst.script.appium.ITest;
import net.kiwox.dst.script.appium.TestUtils;
import net.kiwox.dst.script.enums.EnumCodeProcessTests;
import net.kiwox.dst.script.pojo.TestResult;
import net.kiwox.dst.script.pojo.TestResultDetailEntelApp;
import net.kiwox.dst.script.pojo.TestResultEntelApp;

public class EntelPeruAppTestVerifyReceipt implements ITest {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntelPeruAppTestVerifyPostPaidBalance.class);
	private static final String APP_PACKAGE = "com.entel.movil";
	private static final String APP_ACTIVITY = ".MainActivity";

	private String phoneNumber;
	private String verificationCode;

	private TestResultEntelApp testResult;

	public static final Map<String, AbstractMap.SimpleEntry<String, String>> STEPS_TEST;
	static {
		STEPS_TEST = new HashMap<String, AbstractMap.SimpleEntry<String, String>>();
	}

	public EntelPeruAppTestVerifyReceipt(String phoneNumber, String verificationCode) {
		this.phoneNumber = phoneNumber;
		this.verificationCode = verificationCode;
		this.testResult = new TestResultEntelApp();

		STEPS_TEST.put("Step01", new AbstractMap.SimpleEntry<>("DST-ENTEL_PERU-D003-E001", "Visualizar Recibo"));
		STEPS_TEST.put("Step02", new AbstractMap.SimpleEntry<>("DST-ENTEL_PERU-D003-E002", "Acceder al Recibo Actual"));

	}

	@Override
	public TestResult runTest(AndroidDriver<AndroidElement> driver) throws InterruptedException {
		// TODO: separte startup tests configurations
		// Start activity
		Activity activity = new Activity(APP_PACKAGE, APP_ACTIVITY);
		activity.setAppWaitPackage(APP_PACKAGE);
		activity.setAppWaitActivity("com.entel.*");
		driver.startActivity(activity);
		// WebDriverWait wait = new WebDriverWait(driver, TestUtils.getWaitTimeout());

		// Wait for an expected activity
		String contentPath = "//*[@resource-id='android:id/content']";
		testResult = new EntelPeruAppTestSignIn(phoneNumber, verificationCode).automateStepsSignIn(driver, contentPath);

		// closePromotionPopUp(driver);
		verifyReceipt(driver, contentPath);
		return testResult;
	}

	public void verifyReceipt(AndroidDriver<AndroidElement> driver, String contentPath) throws InterruptedException {
		AbstractMap.SimpleEntry<String, String> errorStep = STEPS_TEST.get("Step01");
		String currentStepErrorCode = errorStep.getKey(), currentStepDetail = errorStep.getValue();

		testResult.setCode(EnumCodeProcessTests.VERIFY_RECEIPT_SUCCESS_ENTEL_PERU_APP.getCode());
		testResult.setMessage(EnumCodeProcessTests.VERIFY_RECEIPT_SUCCESS_ENTEL_PERU_APP.getDescription());
		long totalTimeOutAdd = System.currentTimeMillis(), currentTimeOut = System.currentTimeMillis();
		try {
			WebDriverWait wait = new WebDriverWait(driver, TestUtils.getWaitTimeout());
			String receiptOptionPath = contentPath
					+ "//*[@resource-id='b4-b1-b1-BottomBarItems']/*[@class='android.view.View'][3]";
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(receiptOptionPath))).click();

			/*
			 * Dimension size = driver.manage().window().getSize(); int y1 = (int)
			 * (size.height * 0.70); int y2 = (int) (size.height * 0.10); int x = size.width
			 * / 2;
			 * 
			 * Thread.sleep(5000); TouchAction ts = new TouchAction(driver);
			 * ts.press(PointOption.point(x, y1))
			 * .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
			 * .moveTo(PointOption.point(x, y2)) .release() .perform();
			 */

			String downloadReceiptTextPath = contentPath
					+ "//*[@resource-id='b1-b15-b1-Content']//*[@class='android.view.View' and count(./android.view.View) > 3]/*[@class='android.view.View'][2]/*[@class='android.widget.TextView' and @text='Ver recibo actual'][1]";
			String downloadReceiptTextLabel = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath(downloadReceiptTextPath))).getText();

			boolean expectedTextFound = downloadReceiptTextLabel.trim().equals("Ver recibo actual");
			String finalMessage = String.format("Texto \"Ver recibo actual\" %s fue encontrado en el TAB seleccionado.",
					(expectedTextFound ? "si" : "no"));
			testResult.setError(!expectedTextFound);
			if (testResult.isError()) {
				testResult.setCode(EnumCodeProcessTests.VERIFY_RECEIPT_ERROR_ENTEL_PERU_APP.getCode());
				testResult.setErrorMessage(finalMessage);
			}
			TestUtils.saveScreenshot(driver);
			testResult.addItemDetail((new TestResultDetailEntelApp().setCode("DST-ENTEL_PERU-D003-001")
					.setDescription(currentStepDetail).setDetail(currentStepDetail).setErrorDetected(!expectedTextFound)
					.setTime(System.currentTimeMillis() - currentTimeOut)));
			//
			// Stepp 2
			currentTimeOut = System.currentTimeMillis();
			errorStep = STEPS_TEST.get("Step02");
			currentStepErrorCode = errorStep.getKey();
			currentStepDetail = errorStep.getValue();

			try {
				String tabPath = "//*[@text='PE_APP_Personas_RES.icon_card_recibo']";
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(tabPath))).click();
				Thread.sleep(12000);
				TestUtils.saveScreenshot(driver);
				testResult.addItemDetail((new TestResultDetailEntelApp().setCode("DST-ENTEL_PERU-D003-002")
						.setDescription(currentStepDetail).setDetail(currentStepDetail)
						.setErrorDetected(!expectedTextFound).setTime(System.currentTimeMillis() - currentTimeOut)));

			} catch (Exception e) {
				LOGGER.info("No se puede acceder al recibo actual.");
				testResult.setError(true);
				testResult.setCode(EnumCodeProcessTests.VERIFY_RECEIPT_ERROR_ENTEL_PERU_APP.getCode());
				testResult.setErrorMessage("No se puede acceder al recibo actual.");

			}

			//

		} catch (Exception e) {

			testResult.addItemDetail(
					addErrorDetailItem(currentStepErrorCode, currentStepDetail, e.getMessage(), currentTimeOut));

			testResult.setError(true);
			testResult.setCode(EnumCodeProcessTests.VERIFY_RECEIPT_ERROR_ENTEL_PERU_APP.getCode());
			testResult.setMessage(EnumCodeProcessTests.VERIFY_RECEIPT_ERROR_ENTEL_PERU_APP.getDescription());
			testResult.setErrorMessage(e.getMessage());

			TestUtils.saveScreenshot(driver);
		} finally {
			if (testResult.isError()) {
				testResult.setMessage(EnumCodeProcessTests.VERIFY_RECEIPT_ERROR_ENTEL_PERU_APP.getDescription());
			}
			testResult.addTime(System.currentTimeMillis() - totalTimeOutAdd);
			closeApp(driver);
		}
	}
}
