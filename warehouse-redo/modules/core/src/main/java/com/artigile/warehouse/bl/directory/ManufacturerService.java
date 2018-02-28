/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.directory;

import com.artigile.warehouse.dao.ManufacturerDAO;
import com.artigile.warehouse.domain.directory.Manufacturer;
import com.artigile.warehouse.utils.dto.ManufacturerTO;
import com.artigile.warehouse.utils.transofmers.ManufacturerTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class ManufacturerService {
    private ManufacturerDAO manufacturerDAO;

    public ManufacturerService() {
    }

    public void save(ManufacturerTO manufacturerTO) {
        Manufacturer persistentManufacturer = ManufacturerTransformer.transform(manufacturerTO);
        ManufacturerTransformer.update(persistentManufacturer, manufacturerTO);
        manufacturerDAO.save(persistentManufacturer);
        ManufacturerTransformer.update(manufacturerTO, persistentManufacturer);
    }

    public void remove(ManufacturerTO manufacturerTO) {
        Manufacturer manufacturer = manufacturerDAO.get(manufacturerTO.getId());
        if (manufacturer != null) {
            manufacturerDAO.remove(manufacturer);
        }
    }

    public List<ManufacturerTO> getAll() {
        return ManufacturerTransformer.transformList(manufacturerDAO.getAll());
    }

    public Manufacturer getManufacturerById(long manufacturerId) {
        return manufacturerDAO.get(manufacturerId);
    }

    public ManufacturerTO getManufacturerByName(String name) {
        return ManufacturerTransformer.transform(manufacturerDAO.getManufacturerByName(name));
    }

    public boolean isUniqueManufacturerName(String name, long id) {
        Manufacturer manufacturer = manufacturerDAO.getManufacturerByName(name);
        return manufacturer == null || manufacturer.getId() == id;
    }

    //============================ Spring setters ==================================
    public void setManufacturerDAO(ManufacturerDAO manufacturerDAO) {
        this.manufacturerDAO = manufacturerDAO;
    }
}
