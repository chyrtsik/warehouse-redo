/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.printing;

import com.artigile.warehouse.domain.dataimport.StoredFile;

import javax.persistence.*;

/**
 * @author Aliaksandr Chyrtsik
 * @since 08.05.13
 */
@Entity
@DiscriminatorValue("Jasper")
public class JasperPrintTemplateInstance extends PrintTemplateInstance {
    /**
     * Compiled jasper report data to be passed to report engine.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private StoredFile compiledTemplate;

    public StoredFile getCompiledTemplate() {
        return compiledTemplate;
    }

    public void setCompiledTemplate(StoredFile compiledTemplate) {
        this.compiledTemplate = compiledTemplate;
    }
}
