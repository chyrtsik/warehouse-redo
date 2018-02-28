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
import com.artigile.warehouse.domain.contractors.Contact;

import java.util.List;

/**
 * @author Ihar, Dec 14, 2008
 */
public class ContactDAOImpl extends GenericEntityDAO<Contact> implements ContactDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<String> getAllUniqueAppointments() {
        String query =
                " SELECT DISTINCT concat(ucase(substr(appointment, 1, 1)), lcase(substr(appointment, 2))) " +
                " FROM contact " +
                " ORDER BY concat(ucase(substr(appointment, 1, 1)), lcase(substr(appointment, 2))); ";
        return getSession().createSQLQuery(query).list();
    }
}
