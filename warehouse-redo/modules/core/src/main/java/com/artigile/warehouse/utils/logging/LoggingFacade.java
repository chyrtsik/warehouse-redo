/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.logging;

import java.text.MessageFormat;
import java.util.Calendar;

/**
 * @author Shyrik, 13.06.2010
 */

/**
 * Facade for logging.
 */
public final class LoggingFacade {
    private LoggingFacade(){
    }

    /////////////////////////////////////////////////////////////////////////////////
    // Logging of errors.
    /////////////////////////////////////////////////////////////////////////////////

    public static void logError(String errorMessage){
        logError(null, errorMessage, null);
    }

    public static void logError(Throwable error){
        logError(null, null, error);
    }

    public static void logError(String errorMessage, Throwable error){
        logError(null, errorMessage, error);
    }

    public static void logError(Object object, Throwable error){
        logError(object, null, error);
    }

    public static void logError(Object object, String errorMessage){
        logError(object, errorMessage, null);
    }

    /**
     * Logs information about error.
     * @param object object during which method execution error occured.
     * @param errorMessage programmer friendly error message
     * @param error exception data.
     */
    public static void logError(Object object, String errorMessage, Throwable error){
        //Very simple implementation. Use extended logging if needed.
        System.out.println(MessageFormat.format("ERROR [{0}]", Calendar.getInstance().getTime()));
        System.out.println("BEGIN_ERROR");
        if (object != null){
            System.out.println(MessageFormat.format("Error in object: {0} of class {1}", object, object.getClass().getCanonicalName()));
        }
        if (errorMessage != null){
            System.out.println(MessageFormat.format("Error message: {0}", errorMessage));
        }
        if (error != null){
            System.out.println(error.getLocalizedMessage());
            System.out.println("Exception stack trace:");
            error.printStackTrace(System.err);
        }
        System.out.println("END_ERROR");
    }

    /////////////////////////////////////////////////////////////////////////////////
    // Logging of warnings.
    /////////////////////////////////////////////////////////////////////////////////

    public static void logWarning(String warningMessage){
        logWarning(null, warningMessage, null);
    }

    public static void logWarning(Throwable error){
        logWarning(null, null, error);
    }

    public static void logWarning(String warningMessage, Throwable error){
        logWarning(null, warningMessage, error);
    }

    public static void logWarning(Object object, Throwable error){
        logWarning(object, null, error);
    }

    public static void logWarning(Object object, String warningMessage){
        logWarning(object, warningMessage, null);        
    }

    /**
     * Logs warning into warnings log.
     * @param object object during which method execution error occured.
     * @param warningMessage message of warning.
     * @param error exception data (exception, that caused this warning).
     */
    public static void logWarning(Object object, String warningMessage, Throwable error){
        //Very simple implementation. Use extended logging if needed.
        System.out.println(MessageFormat.format("WARNING [{0}]", Calendar.getInstance().getTime()));
        System.out.println("BEGIN_WARNING");
        if (object != null){
            System.out.println(MessageFormat.format("Warning in object: {0} of class {1}", object, object.getClass().getCanonicalName()));
        }
        if (warningMessage != null){
            System.out.println(MessageFormat.format("Warning message: {0}", warningMessage));
        }
        if (error != null){
            System.out.println(error.getLocalizedMessage());
            System.out.println("Exception stack trace:");
            error.printStackTrace(System.out);
        }
        System.out.println("END_WARNING");
    }


    /////////////////////////////////////////////////////////////////////////////////
    // Logging of informational messages.
    /////////////////////////////////////////////////////////////////////////////////

    public static void logInfo(String infoMessage){
        logInfo(null, infoMessage, null);
    }

    public static void logInfo(Throwable error){
        logInfo(null, null, error);
    }

    public static void logInfo(String infoMessage, Throwable error){
        logInfo(null, infoMessage, error);
    }

    public static void logInfo(Object object, Throwable error){
        logInfo(object, null, error);
    }

    public static void logInfo(Object object, String infoMessage){
        logInfo(object, infoMessage, null);
    }

    /**
     * Logs information message into info's log.
     * @param object object during which method info was generated.
     * @param infoMessage message with information.
     * @param error exception data (exception, that caused this info).
     */
    public static void logInfo(Object object, String infoMessage, Throwable error){
        //Very simple implementation. Use extended logging if needed.
        System.out.println(MessageFormat.format("INFO [{0}]", Calendar.getInstance().getTime()));
        System.out.println("BEGIN_INFO");
        if (object != null){
            System.out.println(MessageFormat.format("Info in object: {0} of class {1}", object, object.getClass().getCanonicalName()));
        }
        if (infoMessage != null){
            System.out.println(MessageFormat.format("Info message: {0}", infoMessage));
        }
        if (error != null){
            System.out.println(error.getLocalizedMessage());
            System.out.println("Exception stack trace:");
            error.printStackTrace(System.out);
        }
        System.out.println("END_INFO");
    }

    /////////////////////////////////////////////////////////////////////////////////
    // Logging of debug messages.
    /////////////////////////////////////////////////////////////////////////////////

    public static void logDebug(String debugMessage){
        logDebug(null, debugMessage, null);
    }

    public static void logDebug(Throwable error){
        logDebug(null, null, error);
    }

    public static void logDebug(String debugMessage, Throwable error){
        logDebug(null, debugMessage, error);
    }

    public static void logDebug(Object object, Throwable error){
        logDebug(object, null, error);
    }

    public static void logDebug(Object object, String debugMessage){
        logDebug(object, debugMessage, null);
    }

    /**
     * Logs debugging message into debug log.
     * @param object object during which method execution debug output was generated.
     * @param debugMessage message with debug information.
     * @param error exception data (exception, that caused this debug info).
     */
    public static void logDebug(Object object, String debugMessage, Throwable error){
        //Very simple implementation. Use extended logging if needed.
        System.out.println(MessageFormat.format("DEBUG [{0}]", Calendar.getInstance().getTime()));
        System.out.println("BEGIN_DEBUG");
        if (object != null){
            System.out.println(MessageFormat.format("Debug info in object: {0} of class {1}", object, object.getClass().getCanonicalName()));
        }
        if (debugMessage != null){
            System.out.println(MessageFormat.format("Debug message: {0}", debugMessage));
        }
        if (error != null){
            System.out.println(error.getLocalizedMessage());
            System.out.println("Exception stack trace:");
            error.printStackTrace(System.out);
        }
        System.out.println("END_DEBUG");
    }
}
