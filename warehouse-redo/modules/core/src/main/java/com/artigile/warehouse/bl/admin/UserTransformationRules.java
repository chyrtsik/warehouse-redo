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
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.transofmers.UserTransformer;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for user-related classes.
 */
public class UserTransformationRules {
    public UserTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getUserToUserTORule());
    }

    private EntityTransformRule getUserToUserTORule() {
        //Rule for transformation from User entity to UserTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(User.class);
        rule.setTargetClass(UserTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return UserTransformer.transformUser((User) entity);
            }
        });
        return rule;
    }
}
