/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.utils;

import java.text.MessageFormat;
import java.util.MissingResourceException;

import java.util.ResourceBundle;

/**
 *
 * @author HuyHoang
 */
public final class ResourceMessages {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("system-messages");

    public static String getResource(String key) {
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

    public static String getString(String key, Object... params) {
        try {
            return MessageFormat.format(resourceBundle.getString(key), params);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
