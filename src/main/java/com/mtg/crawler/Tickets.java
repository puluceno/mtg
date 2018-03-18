package com.mtg.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Tickets {
	private static WebDriver driver;

	@BeforeClass
	public static void before() {
		System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
		driver = new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.get(
				"https://account.fifa.com/5a7baeb7-e706-4830-ad9f-103eba126311/oauth2/v2.0/authorize?p=b2c_1a_fifa_signuporsignin&client_Id=64e9afa8-c5c0-413d-882b-bc9e6a81e264&nonce=defaultNonce&redirect_uri=https%3A%2F%2Fwww.fifa.com%2Ftheclub%2Ftools%2Fcomment%2F_ms_login_handler.htmx&scope=openid%20offline_access&response_type=code&response_mode=form_post&state=aHR0cDovL3d3dy5maWZhLmNvbS93b3JsZGN1cC9vcmdhbmlzYXRpb24vdGlja2V0aW5nL3B1cmNoYXNlLmh0bWwjI2xvZ2lu&ui_locales=en");

	}

	@Test
	public void checkForTickets() throws Exception {
		boolean test = true;

		Thread.sleep(3000);
		try {
			WebElement login = driver.findElement(By.id("signInName"));
			login.click();
			login.sendKeys("ghn1712@gmail.com");

			WebElement pass = driver.findElement(By.id("password"));
			pass.click();
			pass.sendKeys("fifaum4vn");

			driver.findElement(By.id("next")).click();

			Thread.sleep(10000);
			String replace = driver.getCurrentUrl().replace("##login", "");
			driver.get(replace);

			Thread.sleep(5000);
			driver.findElement(By.className("qlink-link-wrap")).click();

			List<String> tabs = new ArrayList<>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));

			System.out.println(driver.getPageSource().contains("Match 41"));

			WebElement findGame = driver.findElement(By.partialLinkText("Match 41"));
			System.out.println(findGame);

		} catch (Exception e) {
			e.printStackTrace();
		}

		while (test)
			try {
				WebElement findElement = driver.findElement(By.xpath(
						"//*[@id=\"productListGroupBy\"]/uib-accordion/div/div/div[2]/div/div[41]/div/div[1]/div[2]/div[1]/text()"));
				System.out.println(findElement);
				Thread.sleep(2000);
			} catch (Exception e) {

			}

	}

	@Ignore
	@Test
	public void playAudio() throws Exception {
		File audioFile = new File("src/main/resources/audio/beep.wav");
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
		AudioFormat format = audioStream.getFormat();

		DataLine.Info info = new DataLine.Info(Clip.class, format);

		Clip audioClip = (Clip) AudioSystem.getLine(info);

		audioClip.open(audioStream);
		audioClip.start();
		Thread.sleep(500);

		audioClip.close();
		audioStream.close();

	}
}
