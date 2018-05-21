package com.dinfo.dispatcher.util;

import java.util.Collection;
import java.util.Map;

/**
 * Uncompleted!
 * @author yangxf
 */
public final class ObjectUtil {

    // ============ private constant ============

    /**
     * The value of lower case minus upper case
     */
    private static final byte LIMIT_UPPER_AND_LOWER = 32;

    /**
     * Before the first or after the last
     */
    private static final byte FIRST_NUMBER = 47;
    private static final byte LAST_NUMBER = 58;
    private static final byte FIRST_UPPER_CASE = 64;
    private static final byte LAST_UPPER_CASE = 91;
    private static final byte FIRST_LOWER_CASE = 96;
    private static final byte LAST_LOWER_CASE = 123;

    private ObjectUtil() {
    }

    /**
     * Convert to upper case if the first char is lower case,
     * otherwise nothing happened, then append the prefix to head
     *
     * @param prefix    prefix of the method
     * @param fieldName name of the field
     * @return method name
     */
    public static String upperCamelCase(String prefix, String fieldName) {
        if (isEmpty(fieldName))
            return fieldName;

        int fcLen, pcLen;
        char[] fieldChars = fieldName.toCharArray(),
                prefixChars = goodbyeNull(prefix, "").toCharArray(),
                resultChars = new char[(fcLen = fieldChars.length) + (pcLen = prefixChars.length)];
        if (isLowerCase(fieldChars[0]))
            fieldChars[0] -= LIMIT_UPPER_AND_LOWER;

        System.arraycopy(prefixChars, 0, resultChars, 0, pcLen);
        System.arraycopy(fieldChars, 0, resultChars, pcLen, fcLen);
        return new String(resultChars);
    }

    /**
     * @param arg a string
     * @return true if the string is integer, otherwise false
     */
    public static boolean isInteger(String arg) {
        if (isEmpty(arg))
            return false;
        for (int i = 0; i < arg.length(); i++)
            if (!isNumber(arg.charAt(i)))
                return false;
        return true;
    }

    /**
     * @param ch a character
     * @return true if the character is digit, otherwise false
     */
    public static boolean isNumber(char ch) {
        return ch > FIRST_NUMBER && ch < LAST_NUMBER;
    }

    /**
     * @param ch a character
     * @return true if the character is letter, otherwise false
     */
    public static boolean isLetter(char ch) {
        return isUpperCase(ch) || isLowerCase(ch);
    }

    /**
     * @param ch a character
     * @return true if the character is upper case, otherwise false
     */
    public static boolean isUpperCase(char ch) {
        return ch > FIRST_UPPER_CASE && ch < LAST_UPPER_CASE;
    }

    /**
     * @param ch a character
     * @return true if the character is lower case, otherwise false
     */
    public static boolean isLowerCase(char ch) {
        return ch > FIRST_LOWER_CASE && ch < LAST_LOWER_CASE;
    }

    // ============ null or nonnull or null ... ============

    public static void checkNullAndThrow(Object obj) {
        if (isNull(obj))
            throw new NullPointerException("null");
    }

    public static void checkNullAndThrow(Object obj, String msg) {
        if (isNull(obj))
            throw new NullPointerException(msg);
    }

    public static void checkNullAndThrow(Object... objs) {
        for (int i = 0; i < objs.length; i++)
            checkNullAndThrow(objs[i]);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean nonNull(Object arg) {
        return !isNull(arg);
    }

    public static boolean isEmpty(String arg) {
        return isNull(arg) || arg.length() == 0;
    }

    public static boolean isEmpty(Collection arg) {
        return isNull(arg) || arg.size() == 0;
    }

    public static boolean isEmpty(Map arg) {
        return isNull(arg) || arg.size() == 0;
    }

    public static boolean isEmpty(Object[] arg) {
        return isNull(arg) || arg.length == 0;
    }

    public static boolean nonEmpty(String arg) {
        return !isEmpty(arg);
    }

    public static boolean nonEmpty(Collection arg) {
        return !isEmpty(arg);
    }

    public static boolean nonEmpty(Map arg) {
        return !isEmpty(arg);
    }

    public static boolean nonEmpty(Object[] arg) {
        return !isEmpty(arg);
    }

    public static <T> T goodbyeNull(T t, T defaultValue) {
        return isNull(t) ? defaultValue : t;
    }

    public static String goodbyeEmpty(String t, String defaultValue) {
        return isEmpty(t) ? defaultValue : t;
    }

    public static <T extends Collection> T goodbyeEmpty(T t, T defaultValue) {
        return isEmpty(t) ? defaultValue : t;
    }

    public static <T extends Map> T goodbyeEmpty(T t, T defaultValue) {
        return isEmpty(t) ? defaultValue : t;
    }

    public static Object[] goodbyeEmpty(Object[] t, Object[] defaultValue) {
        return isEmpty(t) ? defaultValue : t;
    }

}
