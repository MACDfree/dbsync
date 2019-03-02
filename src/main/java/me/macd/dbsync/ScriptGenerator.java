package me.macd.dbsync;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ScriptGenerator {
    public static String generator() throws Exception {
        DocumentBuilder domBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = domBuilder.newDocument();
        doc.setXmlStandalone(true);
        
        Element root = doc.createElement("tasks");
        
        
        return "";
    }
}
