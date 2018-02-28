/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.encryption;

import com.artigile.warehouse.utils.StringHexUtils;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class for encryption/decryption of messages.
 * Singleton class.
 *
 * @author Aliaksandr.Chyrtsik, 06.08.11
 */
public final class EncryptionUtils {

    //================= Singleton implementation ==============================
    private static EncryptionUtils instance;

    static {
        instance = new EncryptionUtils();
    }

    public static EncryptionUtils getInstance() {
        return instance;
    }

    //======================== Class implementation =============================

    private static final String secretKeyString = "318cff67f8b82599d1e333aa9bb6b2a5";

    /**
     * Key used for encoding/decoding.
     */
    private SecretKey secretKey;

    private EncryptionUtils() {
        //Encryption initialization.
        secretKey = new SecretKeySpec(StringHexUtils.hexStringToBytes(secretKeyString), "AES");
    }

    /**
     * Encrypts given bytes array.
     *
     * @param bytes string to be encrypted.
     * @return encrypted bytes array.
     */
    public byte[] encryptBytes(byte[] bytes) {
        try {
            Cipher chipher = Cipher.getInstance("AES");
            chipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return chipher.doFinal(bytes);
        } catch (Exception e) {
            LoggingFacade.logError(this, "Error encrypting data.", e);
            throw new RuntimeException("Error encrypting data.", e);
        }
    }

    /**
     * Decrypts given bytes array.
     *
     * @param encryptedBytes encrypted bytes to be decrypted.
     * @return decrypted original bytes.
     */
    public byte[] decryptBytes(byte[] encryptedBytes) {
        try {
            //1. Decrypt bytes using our secret key.
            Cipher desCipher = Cipher.getInstance("AES");
            desCipher.init(Cipher.DECRYPT_MODE, secretKey);
            return desCipher.doFinal(encryptedBytes);
        } catch (Exception e) {
            LoggingFacade.logError(this, "Error decrypting data.", e);
            throw new RuntimeException("Error decrypting data.", e);
        }
    }
}

