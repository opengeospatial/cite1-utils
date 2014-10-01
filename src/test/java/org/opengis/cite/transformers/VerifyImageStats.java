package org.opengis.cite.transformers;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

public class VerifyImageStats {

    private static XPath xpath;
    private static DocumentBuilder parser;

    public VerifyImageStats() {
    }

    @BeforeClass
    public static void initClassFixture() throws ParserConfigurationException {
        xpath = XPathFactory.newInstance().newXPath();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        parser = dbf.newDocumentBuilder();
    }

    @Test
    @Ignore
    public void verifyLakesImageHasWhiteRegion() throws IOException,
            XPathExpressionException {
        ImageStats iut = new ImageStats();
        URL imgURL = getClass().getResource("/img/cite-Lakes.png");
        InputStream imgStats = getClass().getResourceAsStream("/image-stats-white.xml");
        String stats = iut.transform(imgURL.getContent(), imgStats);
        InputSource src = new InputSource(new StringReader(stats));
        Boolean result = (Boolean) xpath.evaluate("(//count[@sample='0xffffff']/text())[1] = '2500'",
                src, XPathConstants.BOOLEAN);
        assertTrue("Expected (//count[@sample='0xffffff']/text())[1] = '2500'", result);
    }

    @Test
    public void verifyYellowImageHasNoWhiteRegion() throws IOException,
            XPathExpressionException {
        ImageStats iut = new ImageStats();
        URL imgURL = getClass().getResource("/img/FFFF80-200x100.png");
        InputStream imgStats = getClass().getResourceAsStream("/image-stats-white.xml");
        String stats = iut.transform(imgURL.getContent(), imgStats);
        InputSource src = new InputSource(new StringReader(stats));
        Boolean result = (Boolean) xpath.evaluate("(//count[@sample='0xffffff']/text())[1] = '0'",
                src, XPathConstants.BOOLEAN);
        assertTrue("Expected (//count[@sample='0xffffff']/text())[1] = '0'", result);
    }

    @Test
    @Ignore
    public void verifyYellowImageHasYellowRegion() throws IOException,
            XPathExpressionException {
        ImageStats iut = new ImageStats();
        URL imgURL = getClass().getResource("/img/FFFF80-200x100.png");
        InputStream imgStats = getClass().getResourceAsStream("/image-stats-yellow.xml");
        String stats = iut.transform(imgURL.getContent(), imgStats);
        InputSource src = new InputSource(new StringReader(stats));
        Boolean result = (Boolean) xpath.evaluate("(//count[@sample='0xffff80']/text())[1] = '10000'",
                src, XPathConstants.BOOLEAN);
        assertTrue("Expected (//count[@sample='0xffff80']/text())[1] = '10000'", result);
    }

    String writeNodeToString(Node node) throws Exception {
        DOMImplementationRegistry domReg = DOMImplementationRegistry
                .newInstance();
        DOMImplementationLS domLS = (DOMImplementationLS) domReg
                .getDOMImplementation("LS");
        LSSerializer serializer = domLS.createLSSerializer();
        serializer.getDomConfig().setParameter("xml-declaration", false);
        return serializer.writeToString(node);
    }
}