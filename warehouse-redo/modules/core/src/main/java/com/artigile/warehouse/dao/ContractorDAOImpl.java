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
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.contractors.Shipping;
import org.hibernate.Query;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * @author Ihar, Dec 10, 2008
 */

public class ContractorDAOImpl extends GenericEntityDAO<Contractor> implements ContractorDAO {

    private static final String UPDATE_PRICE_LIST_REQUEST_DATETIME = "UPDATE Contractor SET priceListRequest=:priceListRequest WHERE id IN (:ids)";


    @Override
    public Contact getContactById(long contactId) {
        List contacts = getSession().createCriteria(Contact.class)
            .add(Restrictions.eq("id", contactId))
            .list();
        return contacts.size() > 0 ? (Contact)contacts.get(0) : null;
    }

    @Override
    public Shipping getShippingById(long shippingId) {
        List shippings = getSession().createCriteria(Shipping.class)
            .add(Restrictions.eq("id", shippingId))
            .list();
        return shippings.size() > 0 ? (Shipping)shippings.get(0) : null;
    }

    @Override
    public Contractor getContractorByUid(String uid) {
        return (Contractor) getSession().createCriteria(Contractor.class)
                .add(Restrictions.eq("uidContractor", uid))
                .uniqueResult();
    }

    @Override
    public List<String> getUidsByIds(List<Long> ids) {
        org.hibernate.Criteria criteria = getSession().createCriteria(Contractor.class);
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("uidContractor"));
        criteria.setProjection(projectionList);

        List<String> results = criteria.add(Restrictions.in("id", ids)).list();
        return results;
    }

    @Override
    public void removeContact(Contact contact) {
        getSession().delete(contact);
    }

    @Override
    public void removeShipping(Shipping shipping) {
        getSession().delete(shipping);
    }

    @Override
    public void updatePriceListRequestDatetime(List<Long> contractorIDs, Date priceListRequestDatetime) {
        Query query = getSession().createQuery(UPDATE_PRICE_LIST_REQUEST_DATETIME);
        query.setParameter("priceListRequest", priceListRequestDatetime);
        query.setParameterList("ids", contractorIDs);
        query.executeUpdate();
    }
}
