package com.qa.tests;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
//esta clase es para los test que se hagan en el LoginPage
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailsPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
//esta es la clase para hacer los test del product page y demas relacionados
public class ProductsTests extends BaseTest {
	  //1.1 primero hay que inicializar la login page y productPage a nivel de clase
	LoginPage loginPage;
	ProductsPage productsPage;
	
	//para guardar el json
	//#Misma shiet que el LoginPage lo de local blablabla
	//creamos un objeto json
	JSONObject loginUsers;
	//y un objeto settingspage
	SettingsPage settingsPage;
	//y un products details
	ProductDetailsPage productDetailsPage;

 //!!!!!!!! HOLA, este mensaje es para comentar que creamos loginUsers.json pq ahi vaos a guardar los datos del login 
	//segun el metodo, o sea para invalid valid etc
	
	  @BeforeClass
	  public void beforeClass() throws IOException {
		  //usamos try por buena practica
			InputStream datais=null;
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
	  public void beforeMethod(Method m) throws JSONException, InterruptedException {
		  loginPage = PageFactory.initElements(getDriver(), LoginPage.class);
		  //2 aca ya ponemos la logica para imprimir el nombre del test
		  System.out.println("\n" + "*********Starting Test: " + m.getName() + "***********" + "\n");
		  //aqui agregamos el login en vez de en los tests
			 productsPage = loginPage.login(loginUsers.getJSONObject("validUser").getString("username"), loginUsers.getJSONObject("validUser").getString("password"));
	  }

	  @AfterMethod
	  public void afterMethod() {
		  closeApp();
		  launchApp();
	  }
	  
	  
	  
	  //1 test case para probar si sale el mensaje de error al poner username invalido
	  @Test
	  public void validateProductOnProductsPage() throws InterruptedException {
		  //4en este caso podemos usar el soft assert que proporciona testng para validar los datos
		  SoftAssert sa = new SoftAssert();
		  //2guardamos esto en una products page para tener ya los valores
		  
		 //3ahora empezamos a validar
		 String SLBTitle = productsPage.getSLBTitle();
		 //6 vamos a agregar los expected text al strings.xml
		 sa.assertEquals(SLBTitle, getStrings().get("products__page_slb_title"));
         String SLBPrice = productsPage.getSLBPrice();
         sa.assertEquals(SLBPrice, getStrings().get("products__page_slb_price"));
         
        /* //ahora hacemos el logout, como extendemos menupage lo podemos usar con prpoductpage
         settingsPage = productsPage.pressSettingsBtn();
         //lo guardamos es settings page pq aya vamos y toca darle logout
         settingsPage.logoutBtn();*/
         
         
         //hacemos assert a todo
         sa.assertAll();
	  }
	  
	  @Test
	  public void validateProductOnProductDetailsPage() throws InterruptedException {
		  SoftAssert sa = new SoftAssert();		  
		 //nos vamos a products detail page
		 productDetailsPage = productsPage.pressSLBTitle();
		 
		 //obtenemos los datos
		 String SLBTitle = productDetailsPage.getSLBTitle();
		 sa.assertEquals(SLBTitle, getStrings().get("products__detail_page_slb_title"));
         String SLBTxt = productDetailsPage.getSLBTxt();
         sa.assertEquals(SLBTxt, getStrings().get("products__page_slb_txt"));
         //10 o lo que sea, ahora queremos poner el de price tambien pero esta afuera del margen del fon, por eso haremos un scroll, en base test esta el metodo
         //aqui llamamos a ese metodo, y de paso guardamos el price pal asserts
         String SLBPrice = productDetailsPage.scrollToSLBPriceAndGetSLBPrice();
         System.out.println(SLBPrice);
         sa.assertEquals(SLBPrice, getStrings().get("products_details_page_slb_price"));
         
         
         //volvemos a product page
         productsPage = productDetailsPage.pressBackToProductsBtn();
         /*
       //ahora hacemos el logout, como extendemos menupage lo podemos usar con prpoductpage
         settingsPage = productsPage.pressSettingsBtn();
         //lo guardamos es settings page pq aya vamos y toca darle logout
         settingsPage.logoutBtn();*/
         sa.assertAll();
	  }




}
