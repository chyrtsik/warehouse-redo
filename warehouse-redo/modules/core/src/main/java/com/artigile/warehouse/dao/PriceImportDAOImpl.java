/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.bl.priceimport.PriceImportFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.priceimport.ContractorPriceImport;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 *
 * @author Valery.Barysok
 */
public class PriceImportDAOImpl extends GenericEntityDAO<ContractorPriceImport> implements ContractorPriceImportDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<ContractorPriceImport> getListByFilter(PriceImportFilter filter) {
        Criteria criteria = getSession().createCriteria(ContractorPriceImport.class);

        if (filter.getMaxResultsCount() != null){
            //Filter restricting mas results to be returned.
            criteria.setMaxResults(filter.getMaxResultsCount());
        }
        //By default sort results by descending id (i.e. by import date time).
        criteria.addOrder(Order.desc("id"));

        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ContractorPriceImport> getListGroupedByContractorID() {
        return (List<ContractorPriceImport>) getSession()
                .createQuery("FROM ContractorPriceImport GROUP BY contractor_id").list();
    }

    @Override
    public void removePrevContractorProducts(long priceImportId) {
        //HQL bug override: Do not use HQL (hibernate version 3.5.6) -- it generates wrong query for deletion (with "cross join").
        getSession().createSQLQuery("delete a " +
                                    "FROM ContractorPriceImport cpimp1 " +
                                    "INNER JOIN ContractorPriceImport cpimp2 on cpimp1.contractor_id=cpimp2.contractor_id " +
                                    "INNER JOIN DataImport imp1 on cpimp1.dataImport_id = imp1.id " +
                                    "INNER JOIN DataImport imp2 on cpimp2.dataImport_id = imp2.id " +
                                    "INNER JOIN ContractorProduct a on a.priceImport_dataImport_id=cpimp1.dataImport_id " +
                                    "WHERE imp1.importDate<imp2.importDate and cpimp2.dataImport_id=:priceImportId")
                .setParameter("priceImportId", priceImportId)
                .executeUpdate();
    }

    public int getTotalItemCount(long priceImportId) {
        Object result = getSession().createQuery("select count(*) from ContractorProduct b where b.priceImport.id=:priceImportId")
                .setParameter("priceImportId", priceImportId)
                .uniqueResult();

        Long cnt = (Long) result;
        return result == null ? 0 : cnt.intValue();
    }
}
