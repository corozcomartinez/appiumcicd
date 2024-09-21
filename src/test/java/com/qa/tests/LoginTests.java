package com.qa.tests;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
//esta clase es para los test que se hagan en el LoginPage
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.reports.ExtentReport;
//!!aqui tambien tenemos que extender basetest
public class LoginTests extends BaseTest {
	  //1.1 primero hay que inicializar la login page y productPage a nivel de clase
	LoginPage loginPage;
	ProductsPage productsPage;
	//para guardar el json
	//#movemos el inputStream al beforeclass por lo de tenerlo localmente, los demas no porque no los manipulamos solo referenciamos o leemos
	//creamos un objeto json
	JSONObject loginUsers;
	
 //!!!!!!!! HOLA, este mensaje es para comentar que creamos loginUsers.json pq ahi vaos a guardar los datos del login 
	//segun el metodo, o sea para invalid valid etc
	
	  @BeforeClass
	  public void beforeClass() throws IOException {
			InputStream datais = null; // recuerda inicializarla con null para k se pueda usar
		  //usamos try por buena practica
		  try {
			  //5 aqui vamos a leer los datos del json loginUsers para usarlos en los tests
		      String dataFileName = "data/loginUsers.json";
		      datais = getClass().getClassLoader().getResourceAsStream(dataFileName);
		      //ahora con este json tokener podemos manejar la info del json que esta en datais
		      JSONTokener tokener = new JSONTokener(datais);
		      //y guardamos el tokener en un objeto json
		      loginUsers = new JSONObject(tokener);
		  }catch(Exception e) {
			  e.printStackTrace();
		  }finally {
			  if(datais != null) {
				  datais.close();
			  }
		  }
		  closeApp();
		  launchApp();

	  }

	  @AfterClass
	  public void afterClass() {
	  }
	  
	  //1.15aqui en el before method, vamos a poner la inicializacion de la LoginPage para no tener que hacerlo en cada test
	  //1.9 ahora aqui en el before tambien pondremos que imprima el nombre del test que esta probandose
	  //1.9 se pone en la clase un obeto de tipo Method
	  @BeforeMethod
	  public void beforeMethod(Method m) {
			System.out.println("start of login before method");
		  loginPage = PageFactory.initElements(getDriver(), LoginPage.class);
		  //2 aca ya ponemos la logica para imprimir el nombre del test
		  System.out.println("\n" + "*********Starting Test: " + m.getName() + "***********" + "\n");
	  }

	  @AfterMethod
	  public void afterMethod() {
			System.out.println("end of Login after method");

	  }
	  
	  
	  
	  
	  
	  //1 test case para probar si sale el mensaje de error al poner username invalido
	  
	  @Test
	  public void invalidUserName() throws InterruptedException {
		  //1.2usamos los metodos del loginPage para meter el user, la password y darle al boton
		  //4.1 como buena practica para manejar errores encerraremos este pedo en un try
		  //4.2 era broma, checa listeners class, esa madre es donde pusimos la logica para el error handling y mas cosas a parte
		  //5.1 ahora vamos a usar los datos del json para sustituirlos aqui, ponemos el nombre del json object y su sub categoria, checa el .json
		  loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username")); //1.3no necesitamos hace return porque ya estamos retornando el objeto de la misma clase y no de otra
		  loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
		  loginPage.pressLoginButton();
		  
		  //1.4ahora guardamos el texto de error en un string, y usamos el metodo de loginpage de obtener ensaj de error
	      String actualErrTxt = loginPage.getErrTxt();
	      //ahora reemplazamos este string de expected por los datos que estan en strings.xml para no hardcodear texto
	      String expectedErrTxt = getStrings().get("err_invalid_user_or_password");
	      System.out.println("Actual Error Text: " + actualErrTxt +"\nExpected Error Text: " + expectedErrTxt);
	      
	      Assert.assertEquals(actualErrTxt, expectedErrTxt);
	  }

	  
	  
	  //1.5 ahora el test case para el caso de contrasena invalida
	  @Test
	  public void invalidPassword() throws InterruptedException {
		  loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
		  loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
		  loginPage.pressLoginButton();
		  
	      String actualErrTxt = loginPage.getErrTxt();
	      String expectedErrTxt = getStrings().get("err_invalid_user_or_password");
	      System.out.println("Actual Error Text: " + actualErrTxt +"\nExpected Error Text: " + expectedErrTxt);
	      
	      Assert.assertEquals(actualErrTxt, expectedErrTxt);
	  }
	  
	  //1.6 y el de succesfull login
	  @Test
	  public void successfullLogin() throws InterruptedException {
		  loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username")); 
		  loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
		  //1.7 como ahora si vamos a irnos a la Product Page que es otra que login page para obtener el titulo, hay que poner esta de btnLogin como un PorductsPage para el return
		  productsPage = loginPage.pressLoginButton(); //1.8 con este modo no necesitamos inicializar el products page en el beforeMethod
		  
	      String actualProductTitle = productsPage.getTitle();
	      String expectedProductTitle = getStrings().get("product_title");
	      System.out.println("Actual Product Title: " + actualProductTitle +"\nExpected Error Text: " + expectedProductTitle);
	      //debemos hacer logout para poder hacer los demas test cases
	      //mas bien hacer un exit and launch app, checa el before method
	      
	      Assert.assertEquals(actualProductTitle, expectedProductTitle);
	  }
	  
	  





}
