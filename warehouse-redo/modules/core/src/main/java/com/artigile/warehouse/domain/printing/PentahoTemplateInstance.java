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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Pentaho (old) print templates.
 * @author Aliaksandr Chyrtsik
 * @since 08.05.13
 */
@Entity
@DiscriminatorValue("Pentaho")
public class PentahoTemplateInstance extends PrintTemplateInstance {
    //Just an empty class for domain model consistence.
}
