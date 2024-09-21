package com.qa.pages;

import org.openqa.selenium.WebElement;

//esta es la page de la parte de settings
import com.qa.BaseTest;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;

//esta es la page del menu principal de la app
public class SettingsPage extends BaseTest {
	@AndroidFindBy(accessibility="test-LOGOUT")private WebElement logoutBtn;
	
	
	public LoginPage  logoutBtn() throws InterruptedException {
		logoutBtn = getDriver().findElement(AppiumBy.accessibilityId("test-LOGOUT"));
		System.out.println("click logout " + logoutBtn);
		click(logoutBtn,"Clicking logout button");
		return new LoginPage();
	}

}
