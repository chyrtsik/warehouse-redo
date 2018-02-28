/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;

import java.util.ArrayList;
import java.util.List;

public final class MeasureUnitTransformer {
    private MeasureUnitTransformer() {
    }

    public static MeasureUnitTO transform(MeasureUnit measureUnit) {
        if (measureUnit == null){
            return null;
        }
        MeasureUnitTO measureUnitTO = new MeasureUnitTO();
        update(measureUnitTO, measureUnit);
        return measureUnitTO;
    }

    public static List<MeasureUnitTO> transformList(List<MeasureUnit> measureUnits) {
        List<MeasureUnitTO> list = new ArrayList<MeasureUnitTO>();
        for (MeasureUnit measureUnit : measureUnits) {
            list.add(transform(measureUnit));
        }
        return list;
    }

    public static MeasureUnit transform(MeasureUnitTO measureUnitTO) {
        if ( measureUnitTO == null ){
            return null;
        }
        MeasureUnit measureUnit = SpringServiceContext.getInstance().getMeasureUnitService().getMeasureById(measureUnitTO.getId());
        if ( measureUnit == null ){
            measureUnit = new MeasureUnit();
        }
        return measureUnit;
    }

    public static void update(MeasureUnit measureUnit, MeasureUnitTO measureUnitTO) {
        measureUnit.setSign(measureUnitTO.getSign());
        measureUnit.setName(measureUnitTO.getName());
        String code = measureUnitTO.getCode();
        measureUnit.setCode(StringUtils.isStringNullOrEmpty(code) ? null : code);
        measureUnit.setNotice(measureUnitTO.getNotice());
        measureUnit.setDefaultMeasureUnit(measureUnitTO.getDefaultMeasureUnit());
    }

    public static void update(MeasureUnitTO measureUnitTO, MeasureUnit measureUnit) {
        measureUnitTO.setId(measureUnit.getId());
        measureUnitTO.setUidMeasureUnit(measureUnit.getUidMeasureUnit());
        measureUnitTO.setSign(measureUnit.getSign());
        measureUnitTO.setName(measureUnit.getName());
        measureUnitTO.setCode(measureUnit.getCode());
        measureUnitTO.setNotice(measureUnit.getNotice());
        measureUnitTO.setDefaultMeasureUnit(measureUnit.isDefaultMeasureUnit());        
    }
}
