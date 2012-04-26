/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.storbioinfogui.util;

import java.io.IOException;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 *
 * @author kid
 */
public class XMLUtility {
    public static  Document parseXml(String filePath) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        //parse using builder to get DOM representation of the XML file
        Document doc = db.parse(filePath); 
        doc.getDocumentElement().normalize();
        return doc;
    }
    public static String getTagValue(String sTag, Element  eElement) {
	NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
	return nValue.getNodeValue();
  }
}
