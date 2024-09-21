package com.qa.pages;

import org.openqa.selenium.WebElement;

import com.qa.BaseTest;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;

//esta es una de las pages que usara el pageFactory
//esta es la del login
public class LoginPage extends BaseTest{
	
	//3.7en BaseTest.java, 1 Aqui: Bien como esta madre implementara pageFactory podemos usar el accurate android findby en vez de findbyelement y eso
	@AndroidFindBy(xpath = "//android.widget.EditText[@content-desc=\"test-Username\"]") WebElement userNameTxtFld; //1.1 localizar el elemento del input username y guardarlo en un webelement
	@AndroidFindBy(accessibility = "test-Password")private WebElement passwordTxtFld;//1.2 same pero con la password y asi ya te das una idea
	@AndroidFindBy(accessibility = "test-LOGIN")private WebElement loginBtn;
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\\\"test-Error message\\\"]/android.widget.TextView")private WebElement errTxt;//1.3es el texto de error del login invalido
	
	
	
	//1.4 ahora creeemos los metodos para las operaciones dentro de la pagina
	
	public LoginPage enterUserName(String userName) throws InterruptedException {
		Thread.sleep(8000);
		System.out.println("Login With " + userName);
		//1.5 el metodo que creamos en baseTest lo vamo a jalar aca perro, recuerda poner extends BaseTest en la clase e importar el package
		userNameTxtFld = getDriver().findElement(AppiumBy.accessibilityId("test-Username"));
		sendKeys(userNameTxtFld, userName,"Login With " + userName);
		return this; 
		//1.6 IMPORTANTE page object model funciona de forma que si tu estas en una pagina (loginpage) y estas usando
		// o haciendo una accion en esa pagina, si esa accion (sendkeys) te lleva a otra page(baseTest) para hacer el metodo
		//,entonces necesitamos devolver el objeto de esa pagina en particular
		//de esta forma no tienes que inicializar esa pagina especifica(no usamos BaseTest base = new BaseTest() por ejemplo, solo nos vamos a la pag, hacemos el metodo y nos retornamos, en este caso como solo estaos 
		// ingresando el username, no estamos navegando a otra pagina asi que solo devolvemos el objeto de la misma clase (LoginPage)
		
	}
	//1.7 bueno ahora hacemos lo mismo pal password y lo demas
	public LoginPage enterPassword(String password) throws InterruptedException {
		System.out.println("Login With " + password);
		passwordTxtFld = getDriver().findElement(AppiumBy.accessibilityId("test-Password"));
		sendKeys(passwordTxtFld,password,"Login With " + password);
		return this;
	}
	
	public ProductsPage pressLoginButton() throws InterruptedException{
		loginBtn = getDriver().findElement(AppiumBy.accessibilityId("test-LOGIN"));
		System.out.println("Login pressing " + loginBtn);
		click(loginBtn,"Pressing Login Button"); 
		//1.8 ahora, como login button nos va a llevar a products page (classe ProductsPage), o sea a otra pagina, debemos retornar el objeto de ProductPage y no el LoginsPage o this.
		return new ProductsPage();
	}
	//1.9 tambien el metodo de obtener texto de error
	public String getErrTxt() throws InterruptedException {
		errTxt = getDriver().findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"Username and password do not match any user in this service.\"]"));
		System.out.println("getting error text " + errTxt);
		return getAttribute(errTxt,"text","Getting Error text");
		
	}
	//2 ahora que ya estan las pages, toca crear las test clases para las  en el package com.qa.tests
	
	  //vamos a creear un metodo login para poderlo usar en los tests de este y de products page
	  
    public ProductsPage login(String username, String password) throws InterruptedException {
    	enterUserName(username);
    	enterPassword(password);
    	//regresamos esto y no products page porque login buton ya devuelve products page
    	return pressLoginButton();

    	
    }
	
}
