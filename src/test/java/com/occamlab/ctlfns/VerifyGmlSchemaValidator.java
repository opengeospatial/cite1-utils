package com.occamlab.ctlfns;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class VerifyGmlSchemaValidator {

    private static DocumentBuilder parser;

    @BeforeClass
    public static void setUpClass() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        parser = dbf.newDocumentBuilder();
    }

    @Test
    public void testCreateGmlSchemaValidator() throws SAXException, IOException {
        Document doc = parser.parse(getClass().getResourceAsStream(
                "/namespaces.xml"));
        GmlSchemaValidator iut = new GmlSchemaValidator(doc);
        assertNotNull(iut);
    }

    @Test
    public void testValidateSchema() throws Exception {
        Document ns = parser.parse(getClass().getResourceAsStream(
                "/namespaces.xml"));
        GmlSchemaValidator iut = new GmlSchemaValidator(ns);
        String baseUri = "";
        Document xsd = parser.parse(getClass().getResourceAsStream(
                "/dataFeatures.xsd"));
        Boolean isValid = (Boolean) iut.validate(baseUri, xsd);
        assertNotNull("Null result from validator.", isValid);
        assertTrue("Unexpected validation result.", isValid);
    }

}
