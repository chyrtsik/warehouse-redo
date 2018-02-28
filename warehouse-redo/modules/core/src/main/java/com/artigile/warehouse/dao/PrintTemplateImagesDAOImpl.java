/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.printing.PrintTemplateImage;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 25.01.2009
 */
public class PrintTemplateImagesDAOImpl extends GenericEntityDAO<PrintTemplateImage> implements PrintTemplateImagesDAO {
    @Override
    public PrintTemplateImage getImageByName(String imageName) {
        List result = getSession()
            .createCriteria(PrintTemplateImage.class)
            .add(Restrictions.eq("name", imageName))
            .list();
        return result.size() > 0 ? (PrintTemplateImage)result.get(0) : null;
    }
}
