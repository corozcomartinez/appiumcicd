package com.qa.pages;

import org.openqa.selenium.WebElement;

import com.qa.MenuPage;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
//page del product
//vamos a extender a menu page porque comparten cosas y elementos y pueden asi compartir metodos
public class ProductDetailsPage extends MenuPage{
	
//localizamos el titlulo del product (o no se pero ese elemento pues)
	@AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Sauce Labs Backpack\"]") private WebElement SLBTitle; 
	@AndroidFindBy(xpath = "//android.widget.TextView[@text=\"carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.\"]") private WebElement SLBTxt; 
    @AndroidFindBy(accessibility = "test-BACK TO PRODUCTS") private WebElement backToPorductsBtn;
   // @AndroidFindBy(accessibility = "test-Price") private WebElement SLBPrice;

	
	public String getSLBTitle() throws InterruptedException {
		Thread.sleep(2000);
		SLBTitle = getDriver().findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"Sauce Labs Backpack\"]"));
		System.out.println("get SLBTile " + SLBTitle);		
		return getAttribute(SLBTitle,"text","Getting SLB Text");
		
	}
	public String getSLBTxt() throws InterruptedException {
		Thread.sleep(2000);
		SLBTxt = getDriver().findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.\"]"));
		System.out.println("get SLBText " + SLBTxt);
		return getAttribute(SLBTxt,"text","Getting SLB Text");
		
	}
	/*public String getSLBPrice() throws InterruptedException {
		Thread.sleep(2000);
		SLBPrice = driver.findElement(AppiumBy.accessibilityId("test-Price"));
		System.out.println("get SLBPrice " + SLBPrice);
		String price = SLBPrice.getText();
		return price;
	}*/
	
		//uno pa scrollear hasta slb
	public String scrollToSLBPriceAndGetSLBPrice() {
		return scrollToElement().getText();
	}
		
	
	
	public ProductsPage pressBackToProductsBtn() throws InterruptedException {
		backToPorductsBtn = getDriver().findElement(AppiumBy.accessibilityId("test-BACK TO PRODUCTS"));
		System.out.println("Login pressing " + backToPorductsBtn);
		click(backToPorductsBtn,"Clicking back to products button");
		return new ProductsPage();
	}
	
	
	
	
	
}
