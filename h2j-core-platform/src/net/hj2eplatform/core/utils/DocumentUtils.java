/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.utils;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * huyhoang for java platform. (h2j-platform)
 *
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public class DocumentUtils {

    private static final String NAMESPACE = "http://hj2eplatform.net";
    private static final String ROOT_ELEMENT = "hj2eplatform:root";
    private static final String header = "header";
    public static final String HEADER_ID = "id";
    public static DocumentBuilderFactory dbf = DocumentBuilderFactory
            .newInstance();

    public static DocumentBuilderFactory instance() {
        return dbf;
    }

    public static Document instanceNewDocument() throws Exception {
        DOMImplementation domImpl = instance().newDocumentBuilder().getDOMImplementation();
        return domImpl.createDocument(NAMESPACE, ROOT_ELEMENT, null);
    }

    public static Element instanceNewDocumentWidthHeader(Element header) throws Exception {
        instanceNewDocument().getDocumentElement().appendChild(header);
        return header;
    }

    public static String getDocumentAsString(Document document) throws Exception {
        OutputFormat output = new OutputFormat(document, "UTF-8", true);
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos, "UTF-8");
        XMLSerializer s = new XMLSerializer(osw, output);
        s.asDOMSerializer();
        s.serialize(document);

        return new String(baos.toByteArray());
    }

    public static ByteArrayOutputStream getDocumentAsOutputStream(Document document) throws Exception {
        DOMSource domSource = new DOMSource(document);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(domSource, new StreamResult(out));
        return out;
    }

    public static Document getDocumentAsString(String xmlContent) throws Exception {
        InputStream is = new ByteArrayInputStream(xmlContent.getBytes());
        Reader reader = new InputStreamReader(is, "UTF-8");

        InputSource iss = new InputSource(reader);
        iss.setEncoding("UTF-8");

        Document doc = instance().newDocumentBuilder().parse(iss);
        return doc;
    }

    public static String buildExpression(String input) {
        StringBuilder rootEls = new StringBuilder(ROOT_ELEMENT);
        if (input.charAt(0) != '/') {
            rootEls.append("/");
        }
        rootEls.append(input);
        return rootEls.toString();
    }
}
