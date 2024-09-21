package com.qa.reports;

import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

//esta clase es para los extent reports con los que vaos a poder loggwear info en un html con muchos detalles imagenes etc, y opciones para customizar aunque eso ultimo es aqui en codigo
public class ExtentReport {
    static ExtentReports extent;
    final static String filePath = "Extent.html";
    static Map<Integer, ExtentTest> extentTestMap = new HashMap();

    //checamos si ya hay una instancia extent report, si no crea uno
    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
        	ExtentSparkReporter spark = new ExtentSparkReporter("ExtentSpark.html");
        	spark.config().setDocumentTitle("Appium FrameWork");
        	spark.config().setReportName("MyApp"); //es mas para customizacion no necesarios
        	spark.config().setTheme(Theme.DARK);//el tema de la pag, claro u popeye's
            extent = new ExtentReports();
            extent.attachReporter(spark);//tamos creando y asiganndo el reporter
        }
        
        return extent;
    }
    

    public static synchronized ExtentTest getTest() {
        return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));//aca leemos el map con el test object para obtenerlo
    }
//este es para iniciar el testcase, como arriba en el getReporter() ya  instanciamos en extent reportrer y asi, aca sustitye extent por getReporter()
    public static synchronized ExtentTest startTest(String testName, String desc) {
    	//aqui estamos creando el extent test object para nuestro test, y le pasamos el nombre del test y la descripcion
        ExtentTest test = getReporter().createTest(testName, desc);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);//usamos el hashmap para mapear el test con el thread actual

        return test;
    }


}
