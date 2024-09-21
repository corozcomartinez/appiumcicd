package com.qa.pages;

import org.openqa.selenium.WebElement;

import com.qa.MenuPage;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
//page del product
//vamos a extender a menu page porque comparten cosas y elementos y pueden asi compartir metodos
public class ProductsPage extends MenuPage{
	
//localizamos el titlulo del product (o no se pero ese elemento pues)
	@AndroidFindBy(xpath = "//android.widget.TextView[@text=\"PRODUCTS\"]") private WebElement productTitleTxt; 
	@AndroidFindBy(xpath = "//android.widget.TextView[@content-desc=\"test-Item title\" and @text=\"Sauce Labs Backpack\"]") private WebElement SLBTitle; 
	@AndroidFindBy(xpath = "//android.widget.TextView[@content-desc=\"test-Price\" and @text=\"$29.99\"]") private WebElement SLBPrice; 

	//1 y ahora hacemos los metodo de esta pag que es obtener el titulo 
	public String getTitle() throws InterruptedException {
		Thread.sleep(3000);
		productTitleTxt = getDriver().findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"PRODUCTS\"]"));
		System.out.println("get Product Title: " + productTitleTxt);
		return getAttribute(productTitleTxt,"text","Getting Product Text");
		
	}
	
	public String getSLBTitle() throws InterruptedException {
		Thread.sleep(2000);
		SLBTitle = getDriver().findElement(AppiumBy.xpath("//android.widget.TextView[@content-desc=\"test-Item title\" and @text=\"Sauce Labs Backpack\"]"));
		System.out.println("get SLBTitle " + SLBTitle);
		return getAttribute(SLBTitle,"text","Getting SLB Title");
		
	}
	public String getSLBPrice() throws InterruptedException {
		Thread.sleep(2000);
		SLBPrice = getDriver().findElement(AppiumBy.xpath("//android.widget.TextView[@content-desc=\"test-Price\" and @text=\"$29.99\"]"));
		System.out.println("get SLBPrice " + SLBPrice);
		return getAttribute(SLBPrice,"text","Getting SLB Price");
		
	}
	
	public ProductDetailsPage pressSLBTitle() throws InterruptedException {
		Thread.sleep(2000);
		SLBTitle = getDriver().findElement(AppiumBy.xpath("//android.widget.TextView[@content-desc=\"test-Item title\" and @text=\"Sauce Labs Backpack\"]"));
		System.out.println("click slb title " + SLBTitle);
		click(SLBTitle,"Clicking SLB title");
		return new ProductDetailsPage();
	}
	
	
	
	
	
}
