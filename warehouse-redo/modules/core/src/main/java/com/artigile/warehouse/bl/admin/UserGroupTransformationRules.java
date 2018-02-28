/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.admin;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.admin.UserGroup;
import com.artigile.warehouse.utils.dto.UserGroupTO;
import com.artigile.warehouse.utils.transofmers.UserTransformer;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for -related classes.
 */
public class UserGroupTransformationRules {
    public UserGroupTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getUserGroupToUserGroupTORule());
    }

    private EntityTransformRule getUserGroupToUserGroupTORule() {
        //Rule for transformation from UserGroup entity to UserGroupTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(UserGroup.class);
        rule.setTargetClass(UserGroupTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return UserTransformer.transformGroup((UserGroup)entity);
            }
        });
        return rule;
    }
}

