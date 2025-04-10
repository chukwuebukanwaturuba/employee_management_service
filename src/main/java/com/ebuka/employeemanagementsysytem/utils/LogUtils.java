package com.ebuka.employeemanagementsysytem.utils;

import lombok.Getter;
import org.jboss.logging.Logger;

@Getter
public class LogUtils {
    private static final Logger LOGGER = Logger.getLogger(LogUtils.class);

    public static void infoLog(String msg) {
        LOGGER.info(">>>>>>>>>>>>>>  "+msg + " <<<<<<<<<<<<<<<< "+ LogUtils.class.getName());
    }
    public static void debugLog(String msg) {
        LOGGER.debug(">>>>>>>>>>>>>>  "+ msg + " <<<<<<<<<<<<<<<< "+ LogUtils.class.getName());
    }
    public static void warnLog(String msg) {
        LOGGER.warn(">>>>>>>>>>>>>>  "+msg + " <<<<<<<<<<<<<<<< "+ LogUtils.class.getName());
    }


}
