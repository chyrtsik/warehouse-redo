/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Shyrik, 03.01.2009
 */

/**
 * Transfer object for detail groups.
 */
public class DetailGroupTO extends EqualsByIdImpl {
    private long id;
    private String name;
    private Integer sortNum;
    private String description;
    private DetailGroupTO parentGroup;
    private List<DetailGroupTO> childGroups = new ArrayList<DetailGroupTO>();
    private List<DetailTypeTO> detailTypes = new ArrayList<DetailTypeTO>();
    private boolean enableAutoSubGroups;
    private List<Object> filterByGroupingFields;

    public DetailGroupTO(){
    }

    public DetailGroupTO(DetailGroupTO parentGroup) {
        setParentGroup(parentGroup);
    }

    //============================= Operations =================================================
    /**
     * Deep copy of the detail group data.
     * @param src - source of the data.
     */
    public void copyFrom(DetailGroupTO src) {
        setId(src.getId());
        setName(src.getName());
        setSortNum(src.getSortNum());
        setDescription(src.getDescription());
        setDetailTypes(src.getDetailTypes());
        setEnableAutoSubGroups(src.getEnableAutoSubGroups());
        setFilterByGroupingFields(src.getFilterByGroupingFields() == null ? null : new ArrayList<Object>(src.getFilterByGroupingFields()));

        List<DetailGroupTO> copyChildren = new ArrayList<DetailGroupTO>();
        for (DetailGroupTO child : src.getChildGroups()){
            DetailGroupTO copyChild = new DetailGroupTO(this);
            copyChild.copyFrom(child);
            copyChildren.add(copyChild);
        }
        setChildGroups(copyChildren);
    }

    /**
     * Returns tree path of the group.
     * @return - list of names of the groups in the path.
     */
    public List<String> getTreePath() {
        List<String> path = new ArrayList<String>();
        DetailGroupTO curGroup = this;
        while (curGroup != null){
            path.add(0, curGroup.getName());
            curGroup = curGroup.getParentGroup();
        }
        return path;
    }

    /**
     * Gets root group of the groups tree.
     * @return
     */
    public DetailGroupTO getRootGroup() {
        if (isRoot()){
            return this;
        }
        else{
            return getParentGroup().getRootGroup();
        }
    }

    /**
     * Gets child group by name.
     * @param groupName name of child group to be searched.
     * @return child group or null if no group with such name exists.
     */
    public DetailGroupTO findChildGroupByName(String groupName) {
        for (DetailGroupTO group : getChildGroups()){
            if (group.getName().equals(groupName)){
                return group;
            }
        }
        return null;
    }

    /**
     * Gets group path of identifiers (from root group to current group). This "path" can be used for groups
     * identifications (both for persistent and auto grated transient groups).
     * @return identifies from root group till this group.
     */
    public String getIdPath() {
        List<Long> ids = new LinkedList<Long>();
        DetailGroupTO group = this;
        while (group != null){
            ids.add(group.getId());
            group = group.getParentGroup();
        }

        StringBuilder idPathBuilder = new StringBuilder();
        for (int i=ids.size()-1; i>=0; i--){
            if (idPathBuilder.length() > 0){
                idPathBuilder.append(".");
            }
            idPathBuilder.append(ids.get(i));
        }
        return idPathBuilder.toString();
    }

    public long getNextAutoGroupId() {
        long id = -1;
        for (DetailGroupTO groupTO : childGroups){
            if (groupTO.isAutoGroup() && groupTO.getId() <= id){
                id = groupTO.getId() - 1;
            }
        }
        return id;
    }

    public boolean isRoot() {
        return getParentGroup() == null;
    }

    public boolean isNew() {
        return id == 0;
    }

    public boolean isAutoGroup(){
        return id < 0;
    }

    //====================== Getters and setters ===============================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DetailGroupTO getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(DetailGroupTO parentGroup) {
        this.parentGroup = parentGroup;
    }

    public List<DetailGroupTO> getChildGroups() {
        return childGroups;
    }

    public void setChildGroups(List<DetailGroupTO> childGroups) {
        this.childGroups = childGroups;
    }

    public List<DetailTypeTO> getDetailTypes() {
        return detailTypes;
    }

    public void setDetailTypes(List<DetailTypeTO> detailTypes) {
        this.detailTypes = detailTypes;
    }

    public boolean getEnableAutoSubGroups() {
        return enableAutoSubGroups;
    }

    public void setEnableAutoSubGroups(boolean enableAutoSubGroups) {
        this.enableAutoSubGroups = enableAutoSubGroups;
    }

    public List<Object> getFilterByGroupingFields() {
        return filterByGroupingFields;
    }

    public void setFilterByGroupingFields(List<Object> filterByGroupingFields) {
        this.filterByGroupingFields = filterByGroupingFields;
    }
}
