/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.printing.PrintTemplateInstance;
import com.artigile.warehouse.domain.printing.PrintTemplateType;

import java.util.List;

/**
 * @author Aliaksandr.Chyrtsik, 9/11/12
 */
public interface PrintTemplateInstanceDAO extends EntityDAO<PrintTemplateInstance>{
    PrintTemplateInstance getFullTemplateInstanceData(long templateInstanceId);

    PrintTemplateInstance findByName(String name);

    List<PrintTemplateInstance> findByTemplateTypes(PrintTemplateType[] templateTypes);

    void setNewDefaultTemplateInstance(PrintTemplateInstance defaultTemplateInstance);
}
