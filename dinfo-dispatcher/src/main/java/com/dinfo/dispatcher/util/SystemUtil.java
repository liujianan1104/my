package com.dinfo.dispatcher.util;

import static com.dinfo.dispatcher.util.ObjectUtil.goodbyeEmpty;

/**
 * @author yangxf
 * @since 2.0
 */
public class SystemUtil {
    public static final boolean WINDOWS = goodbyeEmpty(System.getProperty("os.name"), "").toLowerCase().startsWith("windows");
}
