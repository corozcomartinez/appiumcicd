package com.qa;
//Todo lo que inicie con # en mis textos es de parallel testing
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.Status;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
//todo lo que inicia con & es mas nuevo qu elo de paralel
//todo lo que inicia con % es lo smas nuevo que o de arriba, es para ver como iniciar appum server automaticamente sin acerlo con cmd ni mierdas, puro codigo 

//esta es la clase base en la que trabajaremos
public class BaseTest {
	//1 inicializamos el driver
	//#1 parallel, vamos a volver nuestras variables threadsafe y para ello usaremos threadlocal
	protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
	//1.4 ahora creamos un objeto properties para cargar nuestras properties del config.properties
	protected static ThreadLocal<Properties> props = new ThreadLocal<Properties>();
	//1.7 creamos un input stream para leer el config.properties(no jalo aqui lo movi abajo en before test creo)
	//agregamos un hashmap por el metodo DOM en utils para leer los mensajes de error y exito
	protected static ThreadLocal<HashMap<String,String>> strings = new ThreadLocal<HashMap<String,String>>();
	//Nuevo ahora crearmos un string DateTime para usarlo en el before
	protected static ThreadLocal<String> dateTime = new ThreadLocal<String>();
	//#2 movemos las variables que solo se usan aqui( InputStreams) y los movemos a nivel de methodo, (ponems en before test su decaracion en v ez de aqui)
	//y un input stream para guardar lo del dom
	ThreadLocal<String> deviceName;
	ThreadLocal<String> platformName;
	//importamos test utils para poder usar el metodo DOM
	TestUtils utils;
     //& vamos a agregar un log4j para poder loggear todo lo qu epasa con la applicacion
	static Logger log = LogManager.getLogger(BaseTest.class.getName());
	//%declaramos una variable para el servicio local de appium drivwr
	private static AppiumDriverLocalService server;
	
	//#Getters y Setters
	public AppiumDriver getDriver() {
		  return driver.get(); //ponemos get por lo de ThreadLocal, este metodo retorna el valor en el en la copia de esta variable usada por el thread actual
	  }
	
	public void setDriver(AppiumDriver driver2) {
		driver.set(driver2); //igual que arriba es para settear el valor del driver en cada thread
	}
	  //comment xd
	public String getDateTime() {
		  return dateTime.get();
	}
	
	public void setDateTime(String dateTime2) {
		dateTime.set(dateTime2);
	}
	
	public Properties getProperties() {
		  return props.get(); 
	  }
	
	public void setProperties(Properties props2) {
		props.set(props2); 
	}
	
	public HashMap<String,String> getStrings() {
		  return strings.get(); 
		  }
	
	public void setStrings(HashMap<String,String> strings2) {
		strings.set(strings2); 
		}
	  
	
	
	//#Fin Getters y Setters
    //#Ahora toca reemplazar todas las menciones de estas variables en esta clase por sus get() y las partes donde (por ejemplo driver = a esto) cambiarlas por set()
   //#todo esto es para logar el thread safety, que los procesos sean metodos
	
	//getter y setters de device y platform

	
	
	
   //3.6 bien ahora creemos un package com.qa.pages para guardar ahi los Pages que usaran esta clase base

	
	
	//NUEVO SUPER NUEVO, esto es para lo de los videos, vamos a crear los after y before methods para poner ahi el inicio y find e la grabacionn
	@BeforeMethod
	public void beforeMethod() {
		System.out.println("start of recording");
		//este metod es para el screen recorder para que inicie la grabacion
		((CanRecordScreen)getDriver()).startRecordingScreen();
	}
	
	
	//#para sincronizar un metodo(info hasta abajo) hay que agregar synchronized al metodo
	@AfterMethod
	public void afterMethod(ITestResult result) {//pasamos Itest para poder crear el directorio donde se guarda
		//aqui paramos la grabacion
		String media = ((CanRecordScreen)getDriver()).stopRecordingScreen();
		//Si quieres que solo se grabe cuando es error en el test pones un if asi:
		//if(result.getStatus() == 2) esto ya que getStatus nos da el status del test, y 2 es el status de failed
		
		//ahora esto como tal nos va dar un string de encode64, asi que de momento lo vamos guardando en un string como vemos arriba
		//usemos results para obtener los parametros del test actual y usarlos para el directiorio, recuerda guardarlo en un Map, hashmap o asi, es igual a lo de screenshot
		Map<String,String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();
		//escribimos el path
		String dir = "Videos" + File.separator + params.get("platformName") +"_"+ params.get("deviceName") + File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();
	
		
		//ahora vamos a no se wey, a crear una nueva File en ese directorio, aun no se pq mas abajo seguro te digo
		//#para sincronizar una variable(info hasta abajo) hay que poner synchronized(variable){ lo que quieres que haga la variable} ejemplo:
		File videoDir = new File(dir);
		
			//ahora creemos la estructura del directorio
			if(!videoDir.exists()){
				videoDir.mkdirs();//basicamente si aun no se crea el directorio lo crea, y si ya existe las sobreescribe
			} //o sea este pedo de arriba del videoDir es para el nombre del video archivo
		

		
		
		
		//y ahora hacemos el metodo para crear e nombre del video y guardarlo en el directorio
		try {
			FileOutputStream stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
			//y ahora con esto decodificamos el code64 para hacerlo
			stream.write(Base64.decodeBase64(media));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end of recording, video saved");

		

	}
	
	//%vamos a crear un before suite para iniciar el appium server
	@BeforeSuite
	public void beforeSuite() {
		server = getAppiumService();
		//%pongamos un if por si el server ya esta activo
		server.start();
		System.out.println("server already running");
		//%para que no salga todo el log del appium en la consola pon:
		server.clearOutPutStreams();
	}
	//%tmb crea un afterSuite para cerrar el server
	@AfterSuite
	public void afterSuite(){
		server.stop();
	}
	
	//%Metodo para obtener el servicio local de appiumdriver
	public AppiumDriverLocalService getAppiumServiceDefault() {
		return AppiumDriverLocalService.buildDefaultService();
	}
	//%si quieres poner alguna opcion custom usa:, en mi caso es para hacer unlog file del server de appium
	public AppiumDriverLocalService getAppiumService() {
		return AppiumDriverLocalService.buildService(new AppiumServiceBuilder().withLogFile(new File("ServerLogs/server.log")));
	}
	
	
	
    
    
    //#cuando quieras hacer paralelo y tengas ios y android hay parametros que no comparten, debes ponerlos como optional:
 //2.2ahora le pasamos los parameters que agregamos en testing.xml al before 
  @Parameters({"emulator","udid","platformName","deviceName"})
  @BeforeTest
  //2.3 asi como agregamos los parameters en @parameters los debemos agregar en el metodo para que los jale del testing y los asigne a estos:
  public void beforeTest(String emulator, String udid,String platformName, String deviceName) throws IOException {

	  log.info("This is Info Message"); //logea info
	  log.error("This is error Message"); //logea errores
	  log.debug("This is debug Message"); //logea cosas de debug
	  log.warn("This is warning Message"); // logea advertencias
   

	  utils = new TestUtils();
	  
	  //Aqui pedimos la fecha actual a la hora de hacer el test
	  //#Esta dando error porque pusimos threadLocal, vamos a agregar getters y setters(creo) de las variables esas para poderlos usar
	  setDateTime(utils.dateTime());  //es e metodo que esta en utils de nuestro proyecto
	  //y a esto le hacemos un metodo aqui en basetets para llamar este dato para despues
	  
	  
	  //1.1 establecemos el create driver session aqui en el before test y lo rodeamos con try catch
		InputStream inputStream = new FileInputStream("C:\\Users\\corozcomartinez\\Appium_Projects\\TDDFramework\\src\\main\\resources\\config.properties");
		InputStream stringsis = null;//la inicializamos aunque sea con null

		//#con props como usa cosas como load y asi vamos acrear una variable local para k haga eso y luego pasarla  a la global
		Properties props = new Properties();
		try {
		  //1.5hacemos una instancia del objeto properties
		  setProperties(new Properties()); 
		  //1.6 y creamos una string para guardar la ubicacon del config.properties
		  //String propiIleName = "config.properties"; //no ponemos ruta pq esta en main/resources
		  //1.8 usamos el input stream para obtener el resource file como una linea stream para poder leerla despues
		  inputStream.getClass().getResourceAsStream("C:\\Users\\corozcomartinez\\Appium_Projects\\TDDFramework\\src\\main\\resources\\config.properties");
          props.load(inputStream);//ahora si cargamos las propiedades hechas stream de config.properties para asignaras al objeto properties como las properties
          //#como aqui ya termino de hacer sus mierdas props, lo pasamos con el set para asignarlo a la global
          setProperties(props);
          
          //Aqui configuramos lo de el xml para poder leer los strings de error text y asi
          String xmlFileName = "strings/strings.xml";
          stringsis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
          setStrings(utils.parseStringXML(stringsis));
          //#para el driver como hacemos la session aqui y eso haermos una variable local driver para usarla aqui y luego se la pasasermos al driver global ya con datos con el setDriver
          AppiumDriver driver;
          
          
		  /*DesiredCapabilities caps = new DesiredCapabilities();
		  
		  //2.1 ahora debemos crear un testing.xml para guardar ahi las capabilities relacionadas a los devices distintos que usaremos (pixel_8, pixel8a etc) para asi no hardcodearlo
		  //2.4 ahora usamos los parameters para traer esos datos del testing.xml, pasarlos a las variables creadas en el metodo(string....) y poderlas usar aca:
			caps.setCapability("platformName", platformName);
			caps.setCapability("appium:deviceName", deviceName);
			caps.setCapability("appium:emulator",emulator);
			caps.setCapability("appium:udid",udid);

			
			
			  //1.9 y ahora usamos ese props.getProperty para obtener las propiedades que tenemos en el config.properties y pasamos al props:
			caps.setCapability("appium:automationName", props.getProperty("androidAutomationName"));
			caps.setCapability("appium:appPackage", props.getProperty("androidAppPackage"));
			caps.setCapability("appium:appActvity", "com.swaglabsmobileapp.SplashActivity");
			//2 como app necesita un path full y nosotros usamos un relative path, hay que agregar esto:
			//2 con esto hacemos que get resource obtenga el path completo de c a resource mas el app location
			String appUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
					+ File.separator + "resources" + File.separator + "app" + File.separator + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";			
			caps.setCapability("appium:app", appUrl);
			URL url = new URL(props.getProperty("appiumURL"));
			  //1.2 cosas como el url, platform name, app y asi, son propiedades(capabilities) globales, que se repiten vaya
			  //1.2 por eso en main crearemos un config.properties donde vamso a poner esas propiedades generales para usarlas siempre desde ese archvio
				        
		   	*/
           
  		    String appUrl = System.getProperty("user.dir") + File.separator+ "src" + File.separator +  "test" + File.separator + "resources"+ File.separator + "app" + File.separator+"Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
  			UiAutomator2Options options = new UiAutomator2Options().setAutomationName("UiAutomator2").setAppPackage(props.getProperty("androidAppPackage")).setAppActivity(props.getProperty("androidAppActivity"));
			URL url = new URL(props.getProperty("appiumURL"));


             //#aqui estamos usando el driver local, no el global
			//inicializamos el driver y la sesion
		    driver = new AndroidDriver(url, options);
			System.out.println("Session ID: "+ driver.getSessionId());
            //2.5 usamos un wait explicit para que cuando sea visible la app inicie todo el pedo
		    setDriver(driver);
	  }catch(Exception e) {
		  e.printStackTrace();
	  }finally {
		  if(stringsis != null) {
			  stringsis.close();
		  }
	  }

  }
  
  
  
  //2.6 haremos un metodo para el wait, esperara a la visibilidad de un webelement que le mandemos, asi lo podemos usar en cualquier caso de wait
  
  public void waitForVisibility(WebElement e) {
	  //2.7 creamos una clase TestUtils en utils, para usar metodo de ella para nuestras clases, entre ellos el de 10 segundos de espera, para no hardcodear 10
	  
	  WebDriverWait wait = new WebDriverWait(getDriver(),TestUtils.WAIT);
	  wait.until(ExpectedConditions.invisibilityOf(e));
  }
  
  //2.8 ahora crearemos metodos para los driver commands como click y eso para reusarlos
  public void click(WebElement e,String log) throws InterruptedException {
	  //2.9 primero llamamos al metodo de la visibility para asegurarnos que es visible y luego click
	  //%%vamos a agregar los logs de cada accion para nuestro extent report en estos metodos para no llenar de logs el codigo normal ni las pages
	  Thread.sleep(2000);
	  ExtentReport.getTest().log(Status.INFO, log);
	  e.click();
  }
  //3 ahora creemos los de send key y get attribute
  public void sendKeys(WebElement e, String keys, String log) throws InterruptedException {
	  //waitForVisibility(e);
	  Thread.sleep(2000);
	  ExtentReport.getTest().log(Status.INFO, log);
	  e.sendKeys(keys);
  }
  
  public String getAttribute(WebElement e, String attribute,String log) throws InterruptedException {
	  Thread.sleep(2000);
	  ExtentReport.getTest().log(Status.INFO, log);
     return e.getAttribute(attribute);
  }
  
  //metodo para salir y entrar a la app para los login
  public void closeApp() {
	  //usa este comando para cerrar una app ya que driver close no es lo mismo
	  ((InteractsWithApps) getDriver()).terminateApp(getProperties().getProperty("androidAppPackage"));
  }
  public void launchApp() {
	  //usa este comando para cerrar una app ya que driver close no es lo mismo
	  ((InteractsWithApps) getDriver()).activateApp(getProperties().getProperty("androidAppPackage"));
  }
  
  //metodo para el scrolling (usalo pa tu porye)
  public WebElement scrollToElement() {
	  //vamos a implementar una logica para scrollear, hasta que un webelemnt que no este a la vista deje de ser null(o sea que se ponga en la vista)
	  //vamos a retornar un web element, que puto pedo no entiendo nada jajajjajaja
	  //es como un metodo para hacer scroll donde buscamos un parent element y lo remplazamos en el texto y al child que es el objetivo tmb supongo 
	  return getDriver().findElement(AppiumBy.androidUIAutomator(
			  "new UiScrollable(new UiSelector()"+".description(\"test-Inventory item page\")).scrollIntoView("+"new UiSelector().description(\"test-Price\"));"));
  }//si no hubiera parent o solo hubierqa uno mas bien, pones en lguar de .description.... parent>\" pones .scrollabe(true)).y lo demas

  //metodo para obtener el driver pa no estarlo llame y llame
  
  
  
  @AfterTest
  public void afterTest() {
	  //3.5 aqui cerramos el driver
	  getDriver().quit();
  }
//#Tambien hay que cambiar las variables de las demas clases por los gets y sets
  //#synchronization lesson : sirve para los test paralelos, si lo pones a nive metodo, un thread espera a que el otro termine el metodo para usarlo, si lo pones a nivel variables pues eso, solo esoeran cuandoola variable dentro del metodo esta siendo usada
//#otra lession pa inicializar un appium server y establecer el puerto usa appium server -p port_no(numero del port wey)
}
