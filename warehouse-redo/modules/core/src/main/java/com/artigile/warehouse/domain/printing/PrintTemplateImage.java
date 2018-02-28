/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.printing;


import javax.persistence.*;

/**
 * @author Shyrik, 24.01.2009
 */

/**
 * Entity for working with image, used in printing process. Each image must have unique name and
 * one image can be used from different printing templates.
 */
@Entity
public class PrintTemplateImage {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Name of the image.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Some user-defined information about image.
     */
    private String notice;

    /**
     * Initial file name of the image.
     */
    private String imageFileName;

    /**
     * Image data.
     */
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] imageData;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte []imageData) {
        this.imageData = imageData;
    }

    public long getVersion() {
        return version;
    }
}
