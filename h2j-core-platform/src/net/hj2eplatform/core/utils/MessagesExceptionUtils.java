/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.utils;

import java.text.MessageFormat;
import net.hj2eplatform.core.exception.BreackByMessagesException;

/**
 *
 * @author HuyHoang
 */
public class MessagesExceptionUtils {

    public static void addErrorMessages(String messages, Object... agr) {
        messages = MessageFormat.format(messages, agr);
        ControllerUtils.addErrorMessage(messages);
        throw new BreackByMessagesException(messages);
    }

    public static void addErrorMessages(String messages) {
        ControllerUtils.addErrorMessage(messages);
        throw new BreackByMessagesException(messages);
    }


    public static void addErrorMessagesKey(String fileName, String key) {
        String messages = ResourceMessagesUtils.getResource(fileName, key);
        ControllerUtils.addErrorMessage(messages);
        throw new BreackByMessagesException(messages);
    }

    public static void addErrorMessagesKey(String fileName, String key, Object... agr) {
        String messages = ResourceMessagesUtils.getResource(fileName, key);
        messages = MessageFormat.format(messages, agr);
        ControllerUtils.addErrorMessage(messages);
        throw new BreackByMessagesException(messages);
    }
}
