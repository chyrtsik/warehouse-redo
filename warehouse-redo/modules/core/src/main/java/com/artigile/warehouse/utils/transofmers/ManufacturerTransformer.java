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

import com.artigile.warehouse.domain.directory.Manufacturer;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ManufacturerTO;

import java.util.ArrayList;
import java.util.List;

public final class ManufacturerTransformer {
    private ManufacturerTransformer() {
    }

    public static ManufacturerTO transform(Manufacturer manufacturer) {
        if (manufacturer == null){
            return null;
        }
        ManufacturerTO manufacturerTO = new ManufacturerTO();
        update(manufacturerTO, manufacturer);
        return manufacturerTO;
    }

    public static List<ManufacturerTO> transformList(List<Manufacturer> manufacturers) {
        List<ManufacturerTO> list = new ArrayList<ManufacturerTO>();
        for (Manufacturer manufacturer : manufacturers) {
            list.add(transform(manufacturer));
        }
        return list;
    }

    public static Manufacturer transform(ManufacturerTO manufacturerTO) {
        if (manufacturerTO == null){
            return null;
        }
        Manufacturer manufacturer = SpringServiceContext.getInstance().getManufacturerService().getManufacturerById(manufacturerTO.getId());
        if (manufacturer == null){
            manufacturer = new Manufacturer();
        }
        return manufacturer;
    }

    public static void update(Manufacturer manufacturer, ManufacturerTO manufacturerTO) {
        manufacturer.setName(manufacturerTO.getName());
        manufacturer.setNotice(manufacturerTO.getNotice());
    }

    public static void update(ManufacturerTO manufacturerTO, Manufacturer manufacturer) {
        manufacturerTO.setId(manufacturer.getId());
        manufacturerTO.setName(manufacturer.getName());
        manufacturerTO.setNotice(manufacturer.getNotice());
    }
}
