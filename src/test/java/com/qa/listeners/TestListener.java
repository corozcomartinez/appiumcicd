package com.qa.listeners;
//**nuevo aca implementaremos los llamados al extent reporter
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.BaseTest;
import com.qa.reports.ExtentReport;

//en esta clase vamos a poner cosas como los exceptions handlers y asi
//implementamos listener
public class TestListener implements ITestListener {
	
	/*//Nuevo 2.0 ahora que ya vimos como tomar screenshots, veremos como grabar video desde que inicia hasta el final del test, fallido o exitoso
	public void onTestStart() {
		
	}
	
	//ahora el de arriba es para cuando inciia el test, pero ITestListener no cuenta con uno cuando termina como tal, por eso usaremos onTestSuccess y onTestFailure para que en ambos se termine de grabar el video, y tengas video falle o no el codigo
	
	public void onTestSuccess(){
		
	}*/ //sin embargo no lo usaremos pues vamos  aver otro modo para grabar y terminar en base test ya que se extiende a los tests, los ponemos en el after y before method para iniciar a grabar y terminar de ello
	
	
	
	
	
	
	//NUEVO
	//EYEYEYEY que pedooo loco aqui vamos a poner parte para tomar screenshots en el testing va?
	//ya tenemos el metodo on test failure asi que hagamoslo ahi
	
	
	//vamos a hacer un metodo para los test fallidos
	//el string writter y el print nos serviran para guardar y mostrat el stacktrace completo para los errores
	public void onTestFailure(ITestResult result) {
		if(result.getThrowable() != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			result.getThrowable().printStackTrace(pw);	
			System.out.println(sw.toString());
			}
		
		
		
		BaseTest base = new BaseTest();
		//con este metodo llamas al driver para tomar el screenshot
		File file = base.getDriver().getScreenshotAs(OutputType.FILE);
		
		//vamos estableciendo la ruta donde se uarda la imagen va?
		//como queremos separar los folders por dispositivo, vamos a usar el ITestrfesult que tiene esos datos
		HashMap<String,String> params =new HashMap<String,String>(result.getTestContext().getCurrentXmlTest().getAllParameters()); //regresa odos los parametros del test actual en el contexto y por ende el device name etc en forma de hasmap
		//ahra si a establecerla, recuerda usar file separator, para el date time checa el utils y en baseTest obtenemos el datetime actual y crearemps un metodo para guardarlo y lo llamamos aca abajo en el path
		//tmb le agregamos la test class name, y usamos result para eso y tmb pa el nombre del test method
		String imagePath = "Screenshots" + File.separator + params.get("platformName")+"_"+params.get("deviceName") + 
				File.separator + base.getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName()+ File.separator + result.getName()+".png";
		//este path es solo para usarlo en el Reporter que pide un path completo ya con todo y el directorio
		String completeImagePath = System.getProperty("user.dir") + File.separator + imagePath;
		//vamos a usar un metodo de java para copiar el screenshot en el path de arriba
		try {
			FileUtils.copyFile(file, new File(imagePath));
			Reporter.log("This is a sample screenchot"); //esto llama al reporter para que haga un log, que es el reporter? no se, es broma es una madre incluida con testng para hacer reportes de los fails
			//y con esto logeamos la imagen, lo de arirba es el texto y abajo la imagen
			Reporter.log("<a href='"+ completeImagePath + "'> <img src='"+ completeImagePath + "' height='100' width='100'/> </a>");
			//y con eso de arriba se nos hace un archiv html en test-Output con el reporte
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//aqui vamos a asignar la captura que guardamos localmente, al report extent para que la tenga en el reporte, y marcara el reporte como fallido con el .fail
		ExtentReport.getTest().fail("Test Failed",MediaEntityBuilder.createScreenCaptureFromPath(completeImagePath).build());
		ExtentReport.getTest().fail(result.getThrowable());
		
		
		
	}//ahora para que este metodo se aplique globalmente y no tengamos que importarlo cada vez
	//debemos agregarlo en el testng.xml

	//%vamos a agregar mas metodos para el extent report
	
	//para iniciar el extent report usamos el on test start (tmb existe onStart pero eso es para proyectos no tests)
	@Override
	public void onTestStart(ITestResult result) {
		//con este metodo iniciamos el test report(se empieza  alogear la info) ponemos el nombre del test y la descripricion del metodo(el test)
		//y tmb la categoria que es el device name y la plataforma porque estamos en mobile testing(es para que en el report se divida por esas categorias) y del mismo modo el nombre del autor (tu servilleta)
		ExtentReport.startTest(result.getName(), result.getMethod().getDescription()).assignCategory("Android_Pixel_8a").assignAuthor("Rizzo");
	}
	
	//ahora queremos cerrar el extenttest cuando se termine el test, pero como puede ser exitosdo, fallido, skip, 2/3 exitoso, hay que definirlos:
	
	@Override
	public void onTestSuccess(ITestResult result) {
		ExtentReport.getTest().log(Status.PASS, "Test Passed");
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentReport.getTest().log(Status.SKIP, "Test Skipped");
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		ExtentReport.getReporter().flush();		
	}

	
}
