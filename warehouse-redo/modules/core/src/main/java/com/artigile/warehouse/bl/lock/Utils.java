/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.lock;

/**
 * @author Valery Barysok, 04.12.2009
 */
public class Utils {
    private Utils() {
    }

    /**
     * Error that is generated when there is an attempt to modify locked entity.
     */
    private static final String ERROR_MESSAGE_LOCKED = "Duplicate entry 'ERR_LOCK_001' for key 'PRIMARY'"; //NOI18N

    /**
     * Error that is generated when exclusive lock cannot be established.
     */
    private static final String ERROR_MESSAGE_EXCLUSIVE_LOCK_FAILED = "Duplicate entry 'ERR_LOCK_002' for key 'PRIMARY'"; //NOI18N

    /**
     * Error that is generated when non exclusive lock cannot be established .
     */
    private static final String ERROR_MESSAGE_NON_EXCLUSIVE_LOCK_FAILED = "Duplicate entry 'ERR_LOCK_003' for key 'PRIMARY'"; //NOI18N

    /**
     * Error that is generated when there is an attempt to modify locked entity.
     */
    private static final String ERROR_MESSAGE_CANNOT_DELETE_OR_UPDATE_PARENT_ROW = "Cannot delete or update a parent row:"; //NOI18N

    /**
     * Error that is generated when there is an attempt to create entity already exists.
     */
    private static final String ERROR_MESSAGE_CANNOT_INSERT = "Duplicate entry"; //NOI18N

    /**
     * Returns true, if given runtime exception is a signal that an entity has an external reference to it.
     * @param e
     * @return
     */
    public static boolean isUsedException(RuntimeException e) {
        return checkExceptionText(e, ERROR_MESSAGE_CANNOT_DELETE_OR_UPDATE_PARENT_ROW, true);
    }

    /**
     * Returns true, if given runtime exception is a signal that an entity already exists.
     * @param e
     * @return
     */
    public static boolean isExistsException(RuntimeException e) {
        return checkExceptionText(e, ERROR_MESSAGE_CANNOT_INSERT, true);
    }

    /**
     * Returns true, if given runtime exception is a signal that entity is locked and cannot be modified.
     * @param e
     * @return
     */
    public static boolean isLockedException(RuntimeException e) {
        return checkExceptionText(e, ERROR_MESSAGE_LOCKED);
    }

    /**
     * Returns true, if given runtime exception is a signal that new lock cannot be established.
     * @param e
     * @return
     */
    public static boolean isLockFailedException(RuntimeException e) {
        return checkExceptionText(e, ERROR_MESSAGE_EXCLUSIVE_LOCK_FAILED) ||
               checkExceptionText(e, ERROR_MESSAGE_NON_EXCLUSIVE_LOCK_FAILED);
    }

    private static boolean checkExceptionText(Throwable e, String errorMessage) {
        return checkExceptionText(e, errorMessage, false);
    }

    private static boolean checkExceptionText(Throwable e, String errorMessage, boolean partial) {
        Throwable cause = e.getCause();
        if (cause != null){
            String message = cause.getMessage();
            if (!partial && message.equals(errorMessage) ||
                    partial && message.contains(errorMessage)) {
                return true;
            }
            else {
                if (cause.getCause() != null) {
                    return checkExceptionText(cause, errorMessage, partial);
                }
            }
        }
        return false;
    }
}
