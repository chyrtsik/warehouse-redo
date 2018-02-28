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

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.contractors.Contact;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.contractors.Shipping;

import java.util.Date;
import java.util.List;

/**
 * @author Ihar, Dec 10, 2008
 */

public interface ContractorDAO extends EntityDAO<Contractor> {

    Contact getContactById(long contactId);

    Shipping getShippingById(long shippingId);

    Contractor getContractorByUid(String uid);

    List<String> getUidsByIds(List<Long> ids);

    void removeContact(Contact contact);

    void removeShipping(Shipping shipping);

    /**
     * Updates datetime of request price list for the given contractors.
     *
     * @param contractorIDs IDs of the contractors for updating
     * @param priceListRequestDatetime New value of request price list
     */
    void updatePriceListRequestDatetime(List<Long> contractorIDs, Date priceListRequestDatetime);
}
