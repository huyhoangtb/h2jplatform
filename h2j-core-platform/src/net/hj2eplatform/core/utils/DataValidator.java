/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.utils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.hj2eplatform.core.exception.ValidateInputException;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public class DataValidator {
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String USERNAME_PATTERN = "^([A-Za-z0-9_-]+[\\.]*){5,25}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
//    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String WEBSITE_PATTERN = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public static void validatePassword(String password) {
        if (password == null || "".compareTo(password) == 0) {
            throw new ValidateInputException("Bạn chưa nhập, vui lòng kiểm tra lại!");
        }
        Pattern emailParttern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = emailParttern.matcher(password);
        if (!matcher.matches()) {
            throw new ValidateInputException("Mật khẩu ko hợp lệ, vui lòng kiểm tra lại!");
        }
    }

    public static Boolean validatePassNotCheckEmptype(String password) {
        Pattern emailParttern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = emailParttern.matcher(password);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static void validateUsernameAsEmail(String username) {
        if (username == null || "".compareTo(username) == 0) {
            throw new ValidateInputException("Bạn chưa nhập tên đăng nhập!");
        }
        Pattern emailParttern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = emailParttern.matcher(username);
        if (!matcher.matches() && !validateEmail(username)) {
            throw new ValidateInputException("Username ko hợp lệ, vui lòng kiểm tra lại!");
        }
        if (username.length() > 50) {
            throw new ValidateInputException("Username ko hợp lệ. Độ dài chỉ từ 6 đến 50.");
        }
    }

    public static void validateUsername(String username) {
        if (username == null || "".compareTo(username) == 0) {
            throw new ValidateInputException("Bạn chưa nhập tên đăng nhập!");
        }
        Pattern emailParttern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = emailParttern.matcher(username);
        if (!matcher.matches()) {
            throw new ValidateInputException("Username ko hợp lệ, vui lòng kiểm tra lại!");
        }
        if (username.length() > 30) {
            throw new ValidateInputException("Username ko hợp lệ. Độ dài chỉ từ 6 đến 30.");
        }
    }

    public static void validateWebsiteAddress(String website) {
        if (website == null || "".compareTo(website.trim()) == 0) {
            return;
        }
        Pattern websiteParttern = Pattern.compile(WEBSITE_PATTERN);
        Matcher matcher = websiteParttern.matcher(website);
        if (!matcher.matches()) {
            throw new ValidateInputException("Địa chỉ website không hợp lệ, vui lòng kiểm tra lại!");
        }
    }

    public static boolean checkValueInArray(List<String> list, String value) {
        if (list == null || list.size() == 0) {
            return false;
        }
        for (String string : list) {
            if (string.compareTo(value) == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkValueInArray(String[] array, String value) {
        if (array == null || array.length == 0) {
            return false;
        }
        for (String string : array) {
            if (string.compareTo(value) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Hàm nhằm mục đích generate ra mot chuỗi ký tự có độ dài nhất định ví dụ:
     * cần tạo ra chuỗi 00001 truyền vào 5, 1
     *
     * @param length
     * @param input
     * @return
     */
    public static String gennerateSequenceNumber(int length, String sequence) {
        if (sequence.length() < length) {
            int leng = length - sequence.length();
            for (int i = 0; i < leng; i++) {
                sequence = "0" + sequence;
            }
        }
        return sequence;
    }

    public static Boolean validateEmail(String email) {
        if (email == null || "".compareTo(email.trim()) == 0) {
            return false;
        }
        Pattern emailParttern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = emailParttern.matcher(email.trim());
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static void validateEmailAdress(String email) {
        if (email == null || "".compareTo(email.trim()) == 0) {
            MessagesExceptionUtils.addErrorMessages("Vui lòng nhập địa chỉ email.");
        }
        Pattern emailParttern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = emailParttern.matcher(email.trim());
        if (!matcher.matches()) {
            MessagesExceptionUtils.addErrorMessages("Địa chỉ email không hợp lệ, vui lòng kiểm tra lại!");
        }
    }

    public static void validateEmailAdress(String email, String notificationMessage) {
        if (email == null || "".compareTo(email) == 0) {
            MessagesExceptionUtils.addErrorMessages("Vui lòng nhập địa chỉ email!" + notificationMessage);
        }
        Pattern emailParttern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = emailParttern.matcher(email);
        if (!matcher.matches()) {
            MessagesExceptionUtils.addErrorMessages("Địa chỉ email không hợp lệ, vui lòng kiểm tra lại!" + notificationMessage);
        }
    }

    public static String deleteSpace(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        StringBuffer stb = new StringBuffer(str);
        deleteSpace(stb);
        return stb.toString();
    }

    public static void deleteSpace(StringBuffer stb) {
        for (int i = 0; i < stb.length(); i++) {
            if (stb.charAt(i) == ' ' && stb.charAt(i + 1) == ' ') {
                stb.deleteCharAt(i + 1);
                i--;
            }
        }
    }

    public static boolean checkCharacter(String str, char c) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }

    public static void validateNotEmpltyAndThanToday(Date value, String emptyMsg, String lestMessage) {
        if (value == null) {
            MessagesExceptionUtils.addErrorMessages(emptyMsg);
        } else if (value.after(new Date())) {
            MessagesExceptionUtils.addErrorMessages(lestMessage);
        }
    }

    public static void validateNotEmpltyAndThanToday(Date value, String messages) {
        if (value == null || value.after(new Date())) {
            MessagesExceptionUtils.addErrorMessages(messages);
        }
    }

    public static void validateEmptyDate(Date value, String messages) {
        if (value == null) {
            MessagesExceptionUtils.addErrorMessages(messages);
        }
    }

    public static void validateEmptyString(String value, String messages) {
        if (value == null || value.trim().compareTo("") == 0) {
            MessagesExceptionUtils.addErrorMessages(messages);
        }
    }

    public static void validateEmptyString(String value, String messages, String... arg) {
        if (value == null || value.trim().compareTo("") == 0) {
            MessagesExceptionUtils.addErrorMessages(messages, arg);
        }
    }

    public static String standardName(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        StringBuffer stb = new StringBuffer(str);
        deleteSpace(stb);
        System.out.println("stb " + stb);
        for (int i = 0; i < stb.length(); i++) {
            if (stb.charAt(i) == ' ' && stb.charAt(i + 1) != ' ') {

                stb.replace(i + 1, i + 2, ("" + stb.charAt(i + 1)).toUpperCase());
//                str.replaceAll(("" + str.charAt(i + 1)), ("" + str.charAt(i + 1)).toUpperCase());

                System.out.println(stb.charAt(i + 1));

            }
        }
        return stb.toString();
    }

    public static void main(String[] agrs) {
        try {
            validatePassword("hoangnh@123");
        } catch (ValidateInputException vie) {
            System.out.println("message: " + vie.getMessage());
        }
    }
}
