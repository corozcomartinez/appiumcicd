package com.qa.utils;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestUtils {

	public static final Duration WAIT = Duration.ofSeconds(10);
	
	
	//bueno puneta, esta madre es el DOM y sirve para leer mamadas de xml, y parsearlas a string y asi, no lo hice, lo copiamos
	public HashMap<String, String> parseStringXML(InputStream file) throws Exception{
		HashMap<String, String> stringMap = new HashMap<String, String>();
		//Get Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		 //construyes el doc
		//Build Document
		Document document = builder.parse(file);
		 //normalizas los datos
		//Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
		 //obtienes el documento 
		//Here comes the root node
		Element root = document.getDocumentElement();
		 //obtienes los string de los elementos
		//Get all elements
		NodeList nList = document.getElementsByTagName("string");
		 
		for (int temp = 0; temp < nList.getLength(); temp++)
		{
		 Node node = nList.item(temp);
		 if (node.getNodeType() == Node.ELEMENT_NODE)
		 {
		    Element eElement = (Element) node;
		    // Store each element key value in map
		    //guardas cada elemento, que es titulo texto, en formato key value del map
		    stringMap.put(eElement.getAttribute("name"), eElement.getTextContent());
		 }
		}
		return stringMap;
	}
	//metodo para obtener el daytime como string para el path
	public String dateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
