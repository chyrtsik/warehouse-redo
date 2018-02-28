package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.sticker.StickerPrintParam;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Valery Barysok, 2013-07-01
 */
public class StickerPrintParamDAOImpl extends GenericEntityDAO<StickerPrintParam> implements StickerPrintParamDAO {

    @Override
    public List<StickerPrintParam> getByDetailType(long detailTypeId) {
        return getSession().createCriteria(StickerPrintParam.class)
                .add(Restrictions.eq("detailType.id", detailTypeId))
                .addOrder(Order.asc("orderNum"))
                .list();
    }
}
