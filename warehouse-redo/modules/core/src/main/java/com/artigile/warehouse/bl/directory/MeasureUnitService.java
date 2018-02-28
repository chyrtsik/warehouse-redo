/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.directory;

import com.artigile.warehouse.dao.MeasureUnitDAO;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.transofmers.MeasureUnitTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class MeasureUnitService {

    private MeasureUnitDAO measureUnitDAO;

    public MeasureUnitService() {
    }

    public void save(MeasureUnitTO measureUnitTO) {
        MeasureUnit persistentMeasureUnit = MeasureUnitTransformer.transform(measureUnitTO);
        MeasureUnitTransformer.update(persistentMeasureUnit, measureUnitTO);
        measureUnitDAO.save(persistentMeasureUnit);
        if ( persistentMeasureUnit.isDefaultMeasureUnit() )
        {
            measureUnitDAO.setNewDefaultMeasureUnit(persistentMeasureUnit);
        }
        MeasureUnitTransformer.update(measureUnitTO, persistentMeasureUnit);
    }

    public void remove(MeasureUnitTO measureUnitTO) {
        MeasureUnit measureUnit = measureUnitDAO.get(measureUnitTO.getId());
        if (measureUnit != null) {
            measureUnitDAO.remove(measureUnit);
        }
    }

    public MeasureUnitTO get(long id) {
        return MeasureUnitTransformer.transform(measureUnitDAO.get(id));
    }

    public List<MeasureUnitTO> getAll() {
        return MeasureUnitTransformer.transformList(measureUnitDAO.getAll());
    }

    public MeasureUnit getMeasureById(long measureId) {
        return measureUnitDAO.get(measureId);
    }

    public MeasureUnit getMeasureUnitByUid(String uid) {
        return measureUnitDAO.getMeasureUnitByUid(uid);
    }

    public MeasureUnitTO findBySign(String measUnitSign) {
        return MeasureUnitTransformer.transform(measureUnitDAO.getMeasureUnitBySign(measUnitSign));
    }

    public MeasureUnitTO findByName(String measUnitName) {
        return MeasureUnitTransformer.transform(measureUnitDAO.getMeasureUnitByName(measUnitName));
    }

    public List<String> getMeasureUnitUidsByIds(List<Long> ids) {
        return measureUnitDAO.getUidsByIds(ids);
    }

    public boolean isUniqueMeasureUnitSign(String sign, long id) {
        MeasureUnit measureUnit = measureUnitDAO.getMeasureUnitBySign(sign);
        return measureUnit == null || measureUnit.getId() == id;
    }

    public boolean isUniqueMeasureUnitName(String name, long id) {
        MeasureUnit measureUnit = measureUnitDAO.getMeasureUnitByName(name);
        return measureUnit == null || measureUnit.getId() == id;
    }

    public boolean isUniqueMeasureUnitCode(String code, long id) {
        MeasureUnit measureUnit = measureUnitDAO.getMeasureUnitByCode(code);
        return measureUnit == null || measureUnit.getId() == id;
    }

    //========================== Spring setters =========================================
    public void setMeasureUnitDAO(MeasureUnitDAO measureUnitDAO) {
        this.measureUnitDAO = measureUnitDAO;
    }
}
