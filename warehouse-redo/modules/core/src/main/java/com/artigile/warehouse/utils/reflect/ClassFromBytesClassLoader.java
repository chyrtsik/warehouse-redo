/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.reflect;

/**
 * Class loader used for loading class from bytes array.
 *
 * @author Aliaksandr.Chyrtsik, 06.08.11
 */
public class ClassFromBytesClassLoader extends ClassLoader {
    /**
     * Name of a class loaded by this class loader.
     */
    private String className;

    /**
     * Bytes of a class (the same as for .class file).
     */
    private byte[] classBytes;

    public ClassFromBytesClassLoader(String className, byte[] classBytes) {
        this.className = className;
        this.classBytes = classBytes;
    }

    protected synchronized Class loadClass(String className, boolean resolve) throws ClassNotFoundException {
        if (!this.className.equals(className)){
            return super.loadClass(className, resolve);
        }

        // 1. Checking is this class already loaded.
        Class cls = findLoadedClass(className);
        if (cls != null) {
            return cls;
        }

        // 2. Turn the byte array into a Class.
        try {
            cls = defineClass(className, classBytes, 0, classBytes.length);
            if (resolve) {
                resolveClass(cls);
            }
            return cls;
        }
        catch (SecurityException e) {
            // Loading core java classes such as java.lang.String
            // is prohibited, throws java.lang.SecurityException.
            // Delegate to parent if not allowed to load class.
            return super.loadClass(className, resolve);
        }
    }
}
