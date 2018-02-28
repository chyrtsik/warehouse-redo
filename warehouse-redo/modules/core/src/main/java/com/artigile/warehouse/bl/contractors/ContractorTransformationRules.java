/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.contractors;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.contractors.Contact;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.contractors.LoadPlace;
import com.artigile.warehouse.domain.contractors.Shipping;
import com.artigile.warehouse.domain.finance.Account;
import com.artigile.warehouse.utils.dto.*;
import com.artigile.warehouse.utils.transofmers.ContractorTransformer;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for Contractor-related classes.
 */
public class ContractorTransformationRules {
    public ContractorTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getContractorToContractorTORule());
        notifier.registerTransformRule(getContactToContactTORule());
        notifier.registerTransformRule(getShippingToShippingTORule());
        notifier.registerTransformRule(getAccountToAccountTORule());
        notifier.registerTransformRule(getLoadPlaceToLoadPlaceTORule());
    }

    private EntityTransformRule getContractorToContractorTORule() {
        //Rule for transformation from Contractor entity to ContractorTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Contractor.class);
        rule.setTargetClass(ContractorTO.class);
        rule.setEntityTransformer(new EntityTransformer() {
            @Override
            public Object transform(Object entity) {
                return ContractorTransformer.transformContractor((Contractor) entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getContactToContactTORule() {
        //Rule for transformation from Contact entity to ContactTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Contact.class);
        rule.setTargetClass(ContactTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return ContractorTransformer.transformContact((Contact) entity);
            }
        });
        return rule;
    }

    public EntityTransformRule getShippingToShippingTORule() {
        //Rule for transformation from Shipping entity to ShippingTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Shipping.class);
        rule.setTargetClass(ShippingTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return ContractorTransformer.transformShipping((Shipping) entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getAccountToAccountTORule() {
        //Rule for transformation from Account entity to AccountTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Account.class);
        rule.setTargetClass(AccountTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return ContractorTransformer.transformAccount((Account) entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getLoadPlaceToLoadPlaceTORule() {
        //Rule for transformation from LoadPlace entity to LoadPlaceTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(LoadPlace.class);
        rule.setTargetClass(LoadPlaceTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return ContractorTransformer.transformLoadPlace((LoadPlace)entity);
            }
        });
        return rule;
    }
}

