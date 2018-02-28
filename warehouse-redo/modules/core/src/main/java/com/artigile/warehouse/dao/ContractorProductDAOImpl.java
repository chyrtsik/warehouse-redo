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

import com.artigile.warehouse.bl.priceimport.ContractorProductFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.dao.util.filters.FilterUtils;
import com.artigile.warehouse.domain.priceimport.ContractorProduct;
import com.artigile.warehouse.utils.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 *
 * @author Valery.Barysok
 */
public class ContractorProductDAOImpl extends GenericEntityDAO<ContractorProduct> implements ContractorProductDAO {

    @Override
    @SuppressWarnings("unchecked")
    public List<ContractorProduct> getListByFilter(ContractorProductFilter filter) {
        // TODO: Criteria API more slower than HQL. Conclusion - use HQL (but it's not so flexible as Criteria API).
        Criteria criteria = getSession().createCriteria(ContractorProduct.class);
        applyFilter(criteria, filter);
        FilterUtils.applyOrdering(criteria, filter.getDataOrder());
        FilterUtils.applyLimitation(criteria, filter.getDataLimit());
        return criteria.list();
    }

    @Override
    public Long getCountByFilter(ContractorProductFilter filter) {
        Criteria criteria = getSession().createCriteria(ContractorProduct.class);
        applyFilter(criteria, filter);

        return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }

    private void applyFilter(Criteria criteria, ContractorProductFilter filter) {
        if (!StringUtils.isStringNullOrEmpty(filter.getNameMask())) {
            if (filter.isIgnoreSpecialSymbols()) {
                criteria.add(Restrictions.ilike("simplifiedName", filter.getNameMask()));
            } else {
                criteria.add(Restrictions.like("name", filter.getNameMask()));
            }
        }
        if (filter.getSelected() != null) {
            criteria.add(Restrictions.eq("selected", filter.getSelected()));
        }
        if (filter.getContractorID() != null) {
            criteria = criteria.createAlias("priceImport", "priceImport");
            criteria.add(Restrictions.eq("priceImport.contractor.id", filter.getContractorID()));
        }
    }
}
