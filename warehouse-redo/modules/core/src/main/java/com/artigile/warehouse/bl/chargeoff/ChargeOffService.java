/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.chargeoff;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.dao.ChargeOffDAO;
import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffTO;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffTOForReport;
import com.artigile.warehouse.utils.transofmers.ChargeOffTransformer;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Shyrik, 09.10.2009
 */
@Transactional(rollbackFor = BusinessException.class)
public class ChargeOffService {
    private ChargeOffDAO chargeOffDAO;

    //================== Construction and initialization ======================================
    public ChargeOffService() {
    }

    //================================= Operations ============================================
    public List<ChargeOffTOForReport> getAllChargeOffs() {
        return ChargeOffTransformer.transformListForReport(chargeOffDAO.getAll());
    }

    public ChargeOffTO getChageOffFullById(long chargeOffId) {
        return ChargeOffTransformer.transform(chargeOffDAO.get(chargeOffId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long getNextAvailableNumber() {
        return chargeOffDAO.getNextAvailableChargeOffNumber();
    }

    public void saveChargeOff(ChargeOff chargeOff) throws BusinessException {
        //Saving charge off.
        chargeOffDAO.save(chargeOff);
    }

    //======================== Spring setters ===========================
    public void setChargeOffDAO(ChargeOffDAO chargeOffDAO) {
        this.chargeOffDAO = chargeOffDAO;
    }
}
