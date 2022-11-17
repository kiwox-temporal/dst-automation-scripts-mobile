package net.kiwox.dst.script.appium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class TestUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);
	
	private static long waitTimeout;
	private static String screenshotFilePath;
	
	private TestUtils() {}

	public static long getWaitTimeout() {
		return waitTimeout;
	}
	public static void setWaitTimeout(long waitTimeout) {
		TestUtils.waitTimeout = waitTimeout;
	}
	
	public static String getScreenshotFilePath() {
		return screenshotFilePath;
	}
	public static void setScreenshotFilePath(String screenshotFilePath) {
		TestUtils.screenshotFilePath = screenshotFilePath;
	}

	public static void saveScreenshot(AndroidDriver<AndroidElement> driver) {
		if (driver == null) {
			return;
		}
		
		File srcFile;
		try {
			srcFile = driver.getScreenshotAs(OutputType.FILE);
		} catch (WebDriverException e) {
			LOGGER.error("Error taking screenshot", e);
			return;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		Path targetPath = Paths.get(screenshotFilePath.replace("?????", dateFormat.format(new Date())));
		try {
			Files.copy(srcFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
			LOGGER.info("Screenshot saved [{}]", targetPath);
		} catch (IOException e) {
			LOGGER.error("Error saving screenshot", e);
		}
	}

}
