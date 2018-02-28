/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.details;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Group of the details for the details catalog.
 * @author Shyrik, 14.12.2008
 */
@Entity
public class DetailGroup {
    /**
     * Primaty key.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Name of the group.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Number of the group in the sorting order (in the range of groups in the same parent group).
     */
    private Integer sortNum;

    /**
     * Description of the group.
     */
    private String description;

    /**
     * Parent group. May be null for the root group.
     */
    @JoinColumn(name = "parentGroup_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL)
    private DetailGroup parentGroup;

    /**
     * Child groups of this group.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentGroup")
    @OrderBy(value="sortNum")
    private List<DetailGroup> childGroups = new ArrayList<DetailGroup>();

    /**
     * Detail types in this group.
     */
    @ManyToMany
    @JoinTable(inverseJoinColumns=@JoinColumn(name="detailType_id"))
    private Set<DetailType> detailTypes;

    /**
     * If true when subgroups may be created automatically (for example, by grouping details using
     * field values as groups).
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean enableAutoSubGroups;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * If set then should be used to filter details in groups by their field values.
     * Values are ordered in the same way as fields grouping numbers (see DetailField.catalogGroupNum).
     * For example, when detail type has fields with catalogGroupNum 1, 2, 4 and values in the filter are
     * "val 1", "var 2", 3.5  then actual filter will be the following:
     *  - field with catalogGroupNum = 1 == "val 1"
     *  - field with catalogGroupNum = 2 == "val 2"
     *  - field with catalogGroupNum = 3 == 3.5
     */
    @Transient
    private List<Object> filterByGroupingFields;

    //================================== Operations ==============================================
    /**
     * Deletes this group from groups tree.
     */
    public void deleteGroup() {
        if (!isRoot()){
            getParentGroup().getChildGroups().remove(this);
            setParentGroup(null);
        }
    }

    /**
     * Add group to the detail groups tree. 
     * @param parentGroup
     */
    public void addGroup(DetailGroup parentGroup) {
        if (parentGroup == null){
            //Group is root.
        }
        else{
            parentGroup.getChildGroups().add(this);
            setParentGroup(parentGroup);
        }
    }

    /**
     * Searches group by it's path in the groups tree. This group is considered to be a root of the subtree.
     * @param groupPath - path of the group.
     * @return
     */
    public DetailGroup getGroupByPath(List<String> groupPath) {
        if (groupPath == null || groupPath.size() == 0){
            return this;
        }

        DetailGroup curGroup = this;
        assert(curGroup.getName().equals(groupPath.get(0)));

        Iterator<String> it = groupPath.iterator();
        assert(it.hasNext());
        it.next();

        while (it.hasNext()){
            String curName = it.next();
            curGroup = curGroup.getChildGroupByName(curName);
        }

        return curGroup;
    }

    /**
     * Finds child group by it's name.
     * @param name
     * @return
     */
    private DetailGroup getChildGroupByName(String name) {
        for (DetailGroup child : getChildGroups()){
            if (child.getName().equals(name)){
                return child;
            }
        }
        return null;
    }

    //================================ Getters and setters =======================================
    public boolean isRoot() {
        return getParentGroup() == null;
    }

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

    public DetailGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(DetailGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    public List<DetailGroup> getChildGroups() {
        return childGroups;
    }

    public void setChildGroups(List<DetailGroup> childGroups) {
        this.childGroups = childGroups;
    }

    public Set<DetailType> getDetailTypes() {
        return detailTypes;
    }

    public void setDetailTypes(Set<DetailType> detailTypes) {
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

    public long getVersion() {
        return version;
    }
}
