/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.details.DetailGroup;
import com.artigile.warehouse.utils.dto.details.DetailCatalogStructureTO;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shyrik, 03.01.2009
 */
public final class DetailGroupsTransformer {
    private DetailGroupsTransformer(){
    }

    /**
     * Transforms list of root groups to the details catalog structure.
     * @param rootGroups
     * @return
     */
    public static DetailCatalogStructureTO rootGroupsToCatalogStructure(List<DetailGroup> rootGroups) {
        DetailCatalogStructureTO catalogStructure = new DetailCatalogStructureTO();
        for (DetailGroup group : rootGroups){
            catalogStructure.getRootGroups().add(transformGroup(group, null));
        }
        return catalogStructure;
    }

    /**
     * Transforms subtree of groups.
     * @param group - root group in the subtree.
     * @param parentGroupTO - TO of the report group, or null, if "group" is the root group of the groups tree.
     * @return
     */
    private static DetailGroupTO transformGroup(DetailGroup group, DetailGroupTO parentGroupTO) {
        DetailGroupTO groupTO = new DetailGroupTO();

        groupTO.setParentGroup(parentGroupTO);
        groupTO.setId(group.getId());
        groupTO.setName(group.getName());
        groupTO.setSortNum(group.getSortNum());
        groupTO.setDescription(group.getDescription());
        groupTO.setDetailTypes(DetailTypesTransformer.transformDetailTypeList(group.getDetailTypes()));
        groupTO.setEnableAutoSubGroups(group.getEnableAutoSubGroups());

        for (DetailGroup child : group.getChildGroups()){
            groupTO.getChildGroups().add(transformGroup(child, groupTO));
        }

        return groupTO;
    }

    /**
     * Syncronize detail catalog structure with it's stored database copy.
     * @param rootGroups - (in, out) root groups, that are stored in the database now.
     * @param deletedGroups - (out) list of deleted groups.
     * @param catalogStructureTO - (in) edited structure of the details catalog.
     */
    public static void updateRootGroupsFromCatalogStructure(List<DetailGroup> rootGroups, List<DetailGroup> deletedGroups, DetailCatalogStructureTO catalogStructureTO) {
        Map<Long, DetailGroup> oldGroups = getAllDetailGroups(rootGroups); //All groups of the old catalog structure.

        Map<Long, DetailGroupTO> editedGroups = new HashMap<Long, DetailGroupTO>(); //Map of edited detail groups.
        List<DetailGroupTO> newGroups = new ArrayList<DetailGroupTO>(); //List of all new detail groups.
        enumAllGroupsFromCatalogStructureTO(catalogStructureTO, editedGroups, newGroups);

        //Synchronizes groups of persistent and edited catalog structure by comparing old and new lists of the
        //detail groups.
        //1. Updating edited detail groups.
        for (DetailGroupTO editedGroup : editedGroups.values()){
            update(oldGroups.get(editedGroup.getId()), editedGroup);
            oldGroups.remove(editedGroup.getId());
        }
        //2. Deleting deleted detail groups.
        for (DetailGroup deletedGroup : oldGroups.values()){
            if (deletedGroup.isRoot()){
                //Mark, that root group needs ti be deleted.
                deletedGroups.add(deletedGroup);
                rootGroups.remove(deletedGroup);
            }
            else{
                //Custom deleting of a non root group.
                deletedGroup.deleteGroup();
                deletedGroups.add(deletedGroup);
            }
        }
        //3. Creating new groups.
        //3.1. Roots groups must be adden to groups list at first.
        for (DetailGroupTO newGroupTO : newGroups){
            if (newGroupTO.isRoot()){
                DetailGroup newGroup = new DetailGroup();
                update(newGroup, newGroupTO);
                rootGroups.add(newGroup);
            }
        }
        //3.2. Processing non root groups.
        for (DetailGroupTO newGroupTO : newGroups){
            if (!newGroupTO.isRoot()){
                //Custom adding new non root group.
                DetailGroup newGroup = new DetailGroup();
                update(newGroup, newGroupTO);

                DetailGroup rootGroup = findGroupByName(rootGroups, newGroupTO.getRootGroup().getName());
                List<String> parentPath = newGroupTO.getParentGroup() == null ? null : newGroupTO.getParentGroup().getTreePath();
                DetailGroup parentGroup = rootGroup.getGroupByPath(parentPath);

                newGroup.addGroup(parentGroup);
            }
        }
    }

    private static DetailGroup findGroupByName(List<DetailGroup> groups, String name) {
        for (DetailGroup group : groups){
            if (group.getName().equals(name)){
                return group;
            }
        }
        return null;
    }

    private static Map<Long, DetailGroup> getAllDetailGroups(List<DetailGroup> rootGroups) {
        Map<Long, DetailGroup> allGroups = new HashMap<Long, DetailGroup>();
        for (DetailGroup rootGroup : rootGroups){
            enumAllDetailGroups(rootGroup, allGroups);
        }
        return allGroups;
    }

    private static void enumAllDetailGroups(DetailGroup group, Map<Long, DetailGroup> groups) {
        groups.put(group.getId(), group);
        for (DetailGroup child : group.getChildGroups()){
            enumAllDetailGroups(child, groups);
        }
    }

    private static void enumAllGroupsFromCatalogStructureTO(DetailCatalogStructureTO catalogStructureTO, Map<Long, DetailGroupTO> editedGroups, List<DetailGroupTO> newGroups) {
        for (DetailGroupTO rootGroup : catalogStructureTO.getRootGroups()){
            enumAllGroupsFromTO(rootGroup, editedGroups, newGroups);            
        }
    }

    private static void enumAllGroupsFromTO(DetailGroupTO group, Map<Long, DetailGroupTO> editedGroups, List<DetailGroupTO> newGroups) {
        if (group.isNew()){
            newGroups.add(group);
        }
        else{
            editedGroups.put(group.getId(), group);
        }
        for (DetailGroupTO child : group.getChildGroups()){
            enumAllGroupsFromTO(child, editedGroups, newGroups);
        }
    }

    /**
     * Synchronizes detail group entity with it's TO.
     * @param detailGroup (out) - entity to be synchronized.
     * @param detailGroupTO (in) - group TO with edited data.
     */
    private static void update(DetailGroup detailGroup, DetailGroupTO detailGroupTO) {
        detailGroup.setName(detailGroupTO.getName());
        detailGroup.setSortNum(detailGroupTO.getSortNum());
        detailGroup.setDescription(detailGroupTO.getDescription());
        detailGroup.setDetailTypes(DetailTypesTransformer.transformDetailTypeListFromTO(detailGroupTO.getDetailTypes()));
        detailGroup.setEnableAutoSubGroups(detailGroupTO.getEnableAutoSubGroups());
    }
}
