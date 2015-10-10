/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import java.util.ResourceBundle;

/**
 *
 * @author HuyHoang
 */
public final class ResourceMessagesUtils {
    public static String TOUR_MESSAGES_RESOURCE = "tour.tour_resources";

    private static Map<String, ResourceBundle> RESOURCES = new HashMap<String, ResourceBundle>();

    public static String getResource(String resources, String key) {
        ResourceBundle resourceBundle = RESOURCES.get(resources);
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle(resources);
            RESOURCES.put(resources, resourceBundle);
        }
        try {
            String value = resourceBundle.getString(key);
            if (value == null) {
                return key;
            }
            return value;
        } catch (Exception ex) {
            return "????" + key;
        }
    }
    public static String getResource(String resources, String key, Object... arg) {
        ResourceBundle resourceBundle = RESOURCES.get(resources);
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle(resources);
            RESOURCES.put(resources, resourceBundle);
        }
        try {
            String value = resourceBundle.getString(key);
            if (value == null) {
                return key;
            }
            String mesages = MessageFormat.format(value, arg);
            return mesages;
        } catch (Exception ex) {
            return "????" + key;
        }
    }

}
