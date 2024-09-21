package com.qa;

import org.openqa.selenium.WebElement;

import com.qa.pages.SettingsPage;

import io.appium.java_client.pagefactory.AndroidFindBy;

//esta es la page del menu principal de la app
public class MenuPage extends BaseTest {
	@AndroidFindBy(xpath="//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView")private WebElement settingsBtn;
	
	
	public SettingsPage  pressSettingsBtn() throws InterruptedException {
		System.out.println("click settings BTN " + settingsBtn);
		click(settingsBtn,"Clicking on settings button");
		return new SettingsPage();
	}

}
