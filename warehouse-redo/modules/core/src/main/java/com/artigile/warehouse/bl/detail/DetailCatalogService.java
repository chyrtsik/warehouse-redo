/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.detail;

import com.artigile.warehouse.dao.DetailFieldDAO;
import com.artigile.warehouse.dao.DetailGroupDAO;
import com.artigile.warehouse.dao.DetailModelDAO;
import com.artigile.warehouse.dao.DetailTypeDAO;
import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailGroup;
import com.artigile.warehouse.domain.details.DetailType;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailCatalogStructureTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.transofmers.DetailGroupsTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author Shyrik, 03.01.2009
 */
@Transactional
public class DetailCatalogService {
    private DetailGroupDAO detailGroupDAO;
    private DetailTypeDAO detailTypeDAO;
    private DetailModelDAO detailModelDAO;
    private DetailFieldDAO detailFieldDAO;

    public DetailCatalogStructureTO getStructure() {
        return DetailGroupsTransformer.rootGroupsToCatalogStructure(detailGroupDAO.getRootGroups());
    }

    /**
     * Constructs details catalog tree with auto grouping of values (by fields with preset grouping order).
     * @return detal catalog structure with auto groups.
     */
    public DetailCatalogStructureTO getStructureWithAutoGroups() {
        DetailCatalogStructureTO catalogStructure = getStructure();
        createAutoSubGroups(catalogStructure.getRootGroups());
        return catalogStructure;
    }

    private void createAutoSubGroups(List<DetailGroupTO> groups) {
        for (DetailGroupTO group : groups){
            if (group.getEnableAutoSubGroups()){
                if (group.getChildGroups().isEmpty()){
                    //Only empty group can have automatic subgroups (to avoid complexity of joining
                    //manually created and automatically created groups).
                    createAutoSubGroupsForGroup(group);
                }
                else{
                    LoggingFacade.logWarning(MessageFormat.format(
                            "Invalid catalog group configuration (group id={0}, name = {1})." +
                            "Group with automatic subgroups cannot have child groups but now {3} child groups are present.",
                            group.getId(), group.getName(), group.getChildGroups().size())
                    );
                }
            }
            else if (!group.getChildGroups().isEmpty()){
                createAutoSubGroups(group.getChildGroups());
            }
        }
    }

    private void createAutoSubGroupsForGroup(DetailGroupTO group) {
        //1. Calculating all possible subgroups names.
        Set<Object[]> subGroupsSequences = new TreeSet<Object[]>(new Comparator<Object[]>() {
            @Override
            public int compare(Object[] sequence1, Object[] sequence2) {
                int minLength = Math.min(sequence1.length, sequence2.length);
                for (int i = 0; i<minLength; i++){
                    Comparable value1 = (Comparable)sequence1[i];
                    Comparable value2 = (Comparable)sequence2[i];
                    if (value1.getClass().isAssignableFrom(value2.getClass())){
                        //Values have the same class. Lets compare them.
                        int result = value1.compareTo(value2);
                        if (result != 0){
                            return result;
                        }
                    }
                    else{
                        //Values have different classes. Just compare class names (to make comparison just working
                        //and returning the same result for each pair of such values).
                        return value1.getClass().getName().compareTo(value2.getClass().getName());
                    }
                }
                if (sequence1.length > sequence2.length){
                    return 1;
                }
                else if (sequence1.length < sequence2.length){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        });
        for (DetailTypeTO detailType : group.getDetailTypes()){
            List<Object[]> subGroupsSequenceForDetailType = getDetailTypeGroupingFieldsValues(detailType);
            if (subGroupsSequenceForDetailType != null){
                subGroupsSequences.addAll(subGroupsSequenceForDetailType);
            }
        }

        //2. Building subgroups from calculates set of subgroups names.
        for (Object[] subgroupsSequence : subGroupsSequences){
            createSubGroupsSequence(group, Arrays.asList(subgroupsSequence), 0);
        }
    }

    private void createSubGroupsSequence(DetailGroupTO group, List<Object> subgroupsSequence, int index) {
        String currentGroupName = getCatalogAutoGroupName(subgroupsSequence.get(index));
        DetailGroupTO childGroup = group.findChildGroupByName(currentGroupName);
        if (childGroup == null){
            //Current group in groups sequence is not presented. Just create one.
            childGroup = new DetailGroupTO();
            childGroup.setId(group.getNextAutoGroupId());
            childGroup.setName(currentGroupName);
            childGroup.setParentGroup(group);
            childGroup.setFilterByGroupingFields(subgroupsSequence.subList(0, index + 1));
            group.getChildGroups().add(childGroup);
        }
        if (index < subgroupsSequence.size() - 1){
            //Recursively create subgroups for current child group.
            createSubGroupsSequence(childGroup, subgroupsSequence, index + 1);
        }
    }

    private String getCatalogAutoGroupName(Object autoGroupFieldValue) {
        if (autoGroupFieldValue instanceof BigDecimal){
            return StringUtils.formatNumber((BigDecimal)autoGroupFieldValue);
        }
        else{
            return autoGroupFieldValue.toString();
        }
    }

    private List<Object[]> getDetailTypeGroupingFieldsValues(DetailTypeTO detailType) {
        List<DetailFieldTO> groupingFieldsTO = detailType.getSortedGroupingFields();
        if (groupingFieldsTO.isEmpty()){
            //No grouping is supported for current detail type.
            return null;
        }

        List<DetailField> groupingFields = new ArrayList<DetailField>(groupingFieldsTO.size());
        for (DetailFieldTO fieldTO : groupingFieldsTO){
            groupingFields.add(detailFieldDAO.get(fieldTO.getId()));
        }

        return detailModelDAO.getAllFieldsValuesCombinations(detailType.getId(), groupingFields);
    }

    public void saveStructure(DetailCatalogStructureTO catalogStructureTO) {
        List<DetailGroup> rootGroups = detailGroupDAO.getRootGroups();
        List<DetailGroup> deletedGroups = new ArrayList<DetailGroup>();
        DetailGroupsTransformer.updateRootGroupsFromCatalogStructure(rootGroups, deletedGroups, catalogStructureTO);

        for (DetailGroup group : rootGroups) {
            detailGroupDAO.save(group);
        }

        for (DetailGroup deletedGroup : deletedGroups){
            detailGroupDAO.remove(deletedGroup);
        }

        refreshDetailTypeSortNumbers(rootGroups, 1);
    }

    /**
     * Refreshes sorm numbers of detail types accoring to the group withing which
     * detail type is located.
     * @param groups groups to be processed.
     * @param startWithNum sort number, from which numeration should start.
     * @return the next available sort number.
     */
    private int refreshDetailTypeSortNumbers(List<DetailGroup> groups, int startWithNum) {
        int currentSortNum = startWithNum;
        for (DetailGroup group : groups){
            //Sort detail types withing current group.
            for (DetailType type : group.getDetailTypes()){
                type.setSortNum(currentSortNum);
                detailTypeDAO.update(type);
                currentSortNum++;
            }
            //Sort detail types withing child groups.
            currentSortNum = refreshDetailTypeSortNumbers(group.getChildGroups(), currentSortNum);
        }
        return currentSortNum;
    }

    public void refreshDetailTypeSortNumbers() {
        List<DetailGroup> rootGroups = detailGroupDAO.getRootGroups();
        refreshDetailTypeSortNumbers(rootGroups, 1);
    }

    //================================= Spring setters ==============================================
    public void setDetailGroupDAO(DetailGroupDAO detailGroupsDAO) {
        this.detailGroupDAO = detailGroupsDAO;
    }

    public void setDetailTypeDAO(DetailTypeDAO detailTypesDAO) {
        this.detailTypeDAO = detailTypesDAO;
    }

    public void setDetailModelDAO(DetailModelDAO detailModelDAO) {
        this.detailModelDAO = detailModelDAO;
    }

    public void setDetailFieldDAO(DetailFieldDAO detailFieldDAO) {
        this.detailFieldDAO = detailFieldDAO;
    }
}
