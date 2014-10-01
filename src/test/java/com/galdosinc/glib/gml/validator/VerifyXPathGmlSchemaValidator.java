package com.galdosinc.glib.gml.validator;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import com.galdosinc.glib.xml.QName;

public class VerifyXPathGmlSchemaValidator {

    private static DocumentBuilder parser;

    @BeforeClass
    public static void initParser() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        parser = dbf.newDocumentBuilder();
    }

    @Test
    public void testDefaultConstructor() {
        XPathGmlSchemaValidator iut = new XPathGmlSchemaValidator();
        assertNotNull(iut);
    }

    @Test
    public void testValidateAppSchema_Ok() throws Exception {
        XPathGmlSchemaValidator iut = new XPathGmlSchemaValidator();
        URL url = getClass().getResource("/dataFeatures.xsd");
        String baseURI = url.toURI().toString();
        Document schema = parser.parse(baseURI);
        String schemaAsText = nodeAsString(schema);
        List<QName> featureTypeNames = new ArrayList<QName>();
        Element result = (Element) iut.execute(baseURI, schemaAsText,
                featureTypeNames);
        assertNotNull("Null result from validator.", result);
        NodeList errors = result.getElementsByTagName("Error");
        assertEquals("Unexpected number of errors.", 0, errors.getLength());
    }

    @Test
    public void testValidateAppSchemaWithError() throws Exception {
        XPathGmlSchemaValidator iut = new XPathGmlSchemaValidator();
        URL url = getClass().getResource("/dataFeatures-invalid.xsd");
        String baseURI = url.toURI().toString();
        String schemaAsText = nodeAsString(parser.parse(baseURI));
        List<QName> featureTypeNames = new ArrayList<QName>();
        Element result = (Element) iut.execute(baseURI, schemaAsText,
                featureTypeNames);
        assertNotNull("Null result from validator.", result);
        NodeList errors = result.getElementsByTagName("Error");
        assertEquals("Unexpected number of errors.", 1, errors.getLength());
    }

    String nodeAsString(Node node) throws Exception {
        DOMImplementationRegistry domReg = DOMImplementationRegistry
                .newInstance();
        DOMImplementationLS domLS = (DOMImplementationLS) domReg
                .getDOMImplementation("LS");
        LSSerializer serializer = domLS.createLSSerializer();
        return serializer.writeToString(node);
    }
}
