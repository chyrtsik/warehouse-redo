package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.sticker.StickerPrintParam;

import java.util.List;

/**
 * @author Valery Barysok, 2013-07-01
 */
public interface StickerPrintParamDAO extends EntityDAO<StickerPrintParam> {

    List<StickerPrintParam> getByDetailType(long detailTypeId);
}
