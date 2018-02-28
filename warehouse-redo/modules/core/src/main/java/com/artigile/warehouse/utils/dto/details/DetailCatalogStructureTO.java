/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 03.01.2009
 */

/**
 * Transfer object for holding detail catalog structure.
 */
public class DetailCatalogStructureTO {
    /**
     * Root groups of the catalog.
     */
    private List<DetailGroupTO> rootGroups = new ArrayList<DetailGroupTO>();


    public DetailCatalogStructureTO() {
    }

    public DetailCatalogStructureTO(DetailCatalogStructureTO src) {
        copyFrom(src);
    }//=================================== Operations ==============================================
    /**
     * Searches group with the same name it the same level, as the given group.
     * @param name - name to be searched.
     * @param group - group, that specifies the level of groups tree.
     * @return
     */
    public DetailGroupTO findGroupByNameAtSameLevel(String name, DetailGroupTO group) {
        if (group.isRoot()){
            for (DetailGroupTO childGroup : rootGroups){
                if (childGroup.getName().equals(name)){
                    return childGroup;
                }
            }
        }
        else{
            for (DetailGroupTO childGroup : group.getParentGroup().getChildGroups()){
                if (childGroup.getName().equals(name)){
                    return childGroup;
                }
            }
        }
        return null;
    }

    /**
     * Deep copy of the detail groups tree of the catalog.
     * @param src - source of the data.
     */
    public void copyFrom(DetailCatalogStructureTO src) {
        rootGroups = new ArrayList<DetailGroupTO>();
        for (DetailGroupTO rootGroup : src.getRootGroups()){
            DetailGroupTO copyGroup = new DetailGroupTO();
            copyGroup.copyFrom(rootGroup);
            rootGroups.add(copyGroup);
        }
    }

    //================================== Getters and setters ======================================
    public List<DetailGroupTO> getRootGroups() {
        return rootGroups;
    }

    public void setRootGroups(List<DetailGroupTO> rootGroups) {
        this.rootGroups = rootGroups;
    }
}
