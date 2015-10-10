/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.utils;

import java.util.HashMap;
import java.util.Map;
import net.hj2eplatform.core.utils.DocumentUtils;
import net.hj2eplatform.core.utils.SystemConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author HuyHoang
 */
public class Languagle {

    public static final String NODE_NAME = "languagle";
    public static final String NODE_ID = "languagle-id";
    public static final String NODE_DETAIL = "detail";
    public static final String LANGUAGLE_AVAIABLE = "languagleAvaiable";
    public static final String LANGUAGLE_DETAULT = "languagleDefault";
    private static final String startXml = "<?xml";
//    private static final String LESS_THAN_SIGN = "<";
//    private static final String LESS_THAN_SIGN_AS = "&lt;";
//    private static final String GREATER_THAN_SIGN = ">";
//    private static final String GREATER_THAN_SIGN_AS = "&gt;";

    public static void put(Map map, String location, String content) {
        map.put(location, content);
        String strs = (String) map.get(LANGUAGLE_AVAIABLE);
        String[] stra = strs.split(",");
        boolean exitsLocation = false;
        for (String str : stra) {
            if (str.compareTo(location) == 0) {
                exitsLocation = true;
            }
        }
        if (!exitsLocation) {
            strs += "," + location;
            map.put(LANGUAGLE_AVAIABLE, strs);
        }
    }

    public static String setValue(String xmlContent, String location, String value) {
        if (value == null) {
            return null;
        }
//        value = value.replace(LESS_THAN_SIGN, LESS_THAN_SIGN_AS);
//        value = value.replace(GREATER_THAN_SIGN, GREATER_THAN_SIGN_AS);
        try {
            Document document = null;
            String defaultLanguagle = SystemConfig.getResource("hj2eplatform.languagle.default");
            String lavai = "";
            if (defaultLanguagle.compareTo(location) == 0) {
                lavai = location;
            } else {
                lavai = defaultLanguagle + "," + location;
            }

            if (xmlContent == null || xmlContent.trim().compareTo("") == 0) {

                if (defaultLanguagle.compareTo(location) == 0) {
                    return value;
                } else {
                    document = DocumentUtils.instanceNewDocument();
                    Element detailNode = document.createElement(NODE_DETAIL);
                    detailNode.setAttribute(LANGUAGLE_AVAIABLE, lavai);
                    detailNode.setAttribute(LANGUAGLE_DETAULT, defaultLanguagle);
                    document.getDocumentElement().appendChild(detailNode);

                    Element elementNode = document.createElement(NODE_NAME);
                    elementNode.setAttribute(NODE_ID, location);
                    elementNode.setTextContent(value);
                    document.getDocumentElement().appendChild(elementNode);

                    Element elementNodeDefault = document.createElement(NODE_NAME);
                    elementNodeDefault.setAttribute(NODE_ID, defaultLanguagle);
                    elementNodeDefault.setTextContent(value);
                    document.getDocumentElement().appendChild(elementNodeDefault);
                    return DocumentUtils.getDocumentAsString(document);
                }
            }

            if (xmlContent.trim().startsWith(startXml)) {
                String exitsLanguagle = location;
                document = DocumentUtils.getDocumentAsString(xmlContent);
                NodeList nodeList = document.getElementsByTagName(NODE_DETAIL);
                String lanAvaiable = nodeList.item(0).getAttributes().getNamedItem(LANGUAGLE_AVAIABLE).getNodeValue();
//                String avaiable = nodeList.item(0).getAttributes().getNamedItem(LANGUAGLE_DETAULT).getNodeValue();
                String[] lans = lanAvaiable.split(",");
                boolean hasAvaiable = false;
                for (int i = 0; i < lans.length; i++) {
                    if (location.compareTo(lans[i]) == 0) {
                        hasAvaiable = true;
                        break;
                    }
                }
                if (!hasAvaiable) { // neu ko ton tai thi tao node moi
                    Element elementNode = document.createElement(NODE_NAME);
                    elementNode.setAttribute(NODE_ID, location);
                    elementNode.setTextContent(value);
                    document.getDocumentElement().appendChild(elementNode);

                    //update cac ngon ngu dang co
                    nodeList = document.getElementsByTagName(NODE_DETAIL);
                    Node nodeDetail = nodeList.item(0);

                    nodeDetail.getAttributes().getNamedItem(LANGUAGLE_AVAIABLE).setNodeValue(lanAvaiable + "," + location);

                    return DocumentUtils.getDocumentAsString(document);
                }
                // neu ton tai thi tim de update
                nodeList = document.getElementsByTagName(NODE_NAME);

                if (nodeList != null && nodeList.getLength() > 0) {
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if (node.getAttributes().getNamedItem(NODE_ID).getNodeValue().compareTo(location) == 0) {
                            node.setTextContent(value);
                            break;
                        }
                    }
                }
                return DocumentUtils.getDocumentAsString(document);
            } else {
                if (defaultLanguagle.compareTo(location) == 0) {
                    return value;
                } else {
                    document = DocumentUtils.instanceNewDocument();
                    Element detailNode = document.createElement(NODE_DETAIL);
                    detailNode.setAttribute(LANGUAGLE_AVAIABLE, lavai);
                    detailNode.setAttribute(LANGUAGLE_DETAULT, defaultLanguagle);
                    document.getDocumentElement().appendChild(detailNode);

                    Element elementNode = document.createElement(NODE_NAME);
                    elementNode.setAttribute(NODE_ID, location);
                    elementNode.setTextContent(value);
                    document.getDocumentElement().appendChild(elementNode);

                    Element elementNodeDefault = document.createElement(NODE_NAME);
                    elementNodeDefault.setAttribute(NODE_ID, defaultLanguagle);
                    elementNodeDefault.setTextContent(xmlContent);
                    document.getDocumentElement().appendChild(elementNodeDefault);
                    return DocumentUtils.getDocumentAsString(document);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static String getValue(String xmlContent, String location) {
        return getValue(xmlContent, location, null);
    }

    public static String getValue(String xmlContent, String location, String returnDefault) {
        if (xmlContent == null) {
            return null;
        }
        
        if (!xmlContent.trim().startsWith(startXml)) {
//            xmlContent = xmlContent.replace(LESS_THAN_SIGN_AS, LESS_THAN_SIGN);
//            xmlContent = xmlContent.replace(GREATER_THAN_SIGN_AS, GREATER_THAN_SIGN);
            if (returnDefault != null) {
                 return returnDefault;
                
            }
            return xmlContent;
        }
        Document document = null;
        try {
            document = DocumentUtils.getDocumentAsString(xmlContent);
        } catch (Exception ex) {
            ex.printStackTrace();
//            xmlContent = xmlContent.replace(LESS_THAN_SIGN_AS, LESS_THAN_SIGN);
//            xmlContent = xmlContent.replace(GREATER_THAN_SIGN_AS, GREATER_THAN_SIGN);
            return xmlContent;
        }

        NodeList nodeList = document.getElementsByTagName(NODE_DETAIL);
        if (nodeList != null && nodeList.getLength() == 1) {
//            nodeList.item(0).getAttributes().getNamedItem(LANGUAGLE_AVAIABLE).getNodeValue();
            String avaiable = nodeList.item(0).getAttributes().getNamedItem(LANGUAGLE_AVAIABLE).getNodeValue();

            String[] lans = avaiable.split(",");
            if (lans != null && lans.length < 1) {
                return "";
            }
            boolean hasAvaiable = false;
            for (int i = 0; i < lans.length; i++) {
                if (location.compareTo(lans[i]) == 0) {
                    hasAvaiable = true;
                    break;
                }
            }
            if (!hasAvaiable) {
                 String langDefault = nodeList.item(0).getAttributes().getNamedItem(LANGUAGLE_DETAULT).getNodeValue();
//                location = avaiable;// neu ngon ngu truyen vao ko ton tai thi lay mac dinh
                location = langDefault;// neu ngon ngu truyen vao ko ton tai thi lay mac dinh
            }
        }

        nodeList = document.getElementsByTagName(NODE_NAME);

        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getAttributes().getNamedItem(NODE_ID).getNodeValue().compareTo(location) == 0) {
                    String value = node.getTextContent();
//                    value = value.replace(LESS_THAN_SIGN_AS, LESS_THAN_SIGN);
//                    value = value.replace(GREATER_THAN_SIGN_AS, GREATER_THAN_SIGN);
                    return node.getTextContent();
                }
            }
        }
        if (returnDefault != null) {
            return returnDefault;
        }
        return "";
        // truong hop khong thay, return defaule;
    }

    public static Map<String, String> getMap(String xmlContent) {

        Map<String, String> map = new HashMap<String, String>();
        if (!xmlContent.trim().startsWith(startXml)) {
            initStart(map, xmlContent);
            return map;
        }
        Document document = null;
        try {
            document = DocumentUtils.getDocumentAsString(xmlContent);
        } catch (Exception ex) {
            initStart(map, xmlContent);
            return map;
        }

        NodeList nodeList = document.getElementsByTagName(NODE_DETAIL);
        if (nodeList != null && nodeList.getLength() == 1) {
            map.put(LANGUAGLE_AVAIABLE, nodeList.item(0).getAttributes().getNamedItem(LANGUAGLE_AVAIABLE).getNodeValue());
            map.put(LANGUAGLE_DETAULT, nodeList.item(0).getAttributes().getNamedItem(LANGUAGLE_DETAULT).getNodeValue());
        }
        nodeList = document.getElementsByTagName(NODE_NAME);
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                map.put(node.getAttributes().getNamedItem(NODE_ID).getNodeValue(), node.getTextContent());
            }
        }
        return map;
    }

    public static void initStart(Map<String, String> map, String value) {
        String defaultLanguagle = SystemConfig.getResource("hj2eplatform.languagle.default");
        map.put(LANGUAGLE_AVAIABLE, defaultLanguagle);
        map.put(LANGUAGLE_DETAULT, defaultLanguagle);
        map.put(defaultLanguagle, value);
    }

    public static String getString(Map<String, String> inputMap) {
        try {
            Document document = DocumentUtils.instanceNewDocument();
            Element detailNode = document.createElement(NODE_DETAIL);
            detailNode.setAttribute(LANGUAGLE_AVAIABLE, inputMap.get(LANGUAGLE_AVAIABLE));
            detailNode.setAttribute(LANGUAGLE_DETAULT, inputMap.get(LANGUAGLE_DETAULT));
            document.getDocumentElement().appendChild(detailNode);
            for (Map.Entry<String, String> entry : inputMap.entrySet()) {
                String key = entry.getKey();
                if (key.equals(LANGUAGLE_AVAIABLE) || key.equals(LANGUAGLE_DETAULT)) {
                    continue;
                }
                String value = entry.getValue();
                Element elms = document.createElement(NODE_NAME);
                elms.setAttribute(NODE_ID, key);
                elms.setTextContent(value);
                document.getDocumentElement().appendChild(elms);
            }
            return DocumentUtils.getDocumentAsString(document);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] agrs) throws Exception {
        Map<String, String> map = getMap("Noi dung ban dau");
        map.put("vii", "tiền tip hướng dẫn viên");
        map.put("en", "Noi dung tieng anh");

        System.out.println(getString(map));

        Map<String, String> map1 = getMap(getString(map));
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            String string = entry.getKey();
            String string1 = entry.getValue();
            System.out.println(string + "         " + string1);
        }

    }
}
