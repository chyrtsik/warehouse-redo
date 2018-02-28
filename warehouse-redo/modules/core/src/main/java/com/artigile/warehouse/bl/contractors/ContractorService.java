/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.contractors;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.finance.AccountService;
import com.artigile.warehouse.dao.*;
import com.artigile.warehouse.domain.contractors.Contact;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.contractors.LoadPlace;
import com.artigile.warehouse.domain.contractors.Shipping;
import com.artigile.warehouse.domain.finance.Account;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.priceimport.ContractorPriceImport;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.*;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.ContractorTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service for Contractor.
 *
 * @author Ihar, Dec 10, 2008
 */

@Transactional(rollbackFor = BusinessException.class)
public class ContractorService {
    private ContractorDAO contractorDAO;
    private CurrencyDAO currencyDAO;
    private LoadPlaceDAO loadPlaceDAO;
    private AccountService accountService;
    private ContactDAO contactDAO;

    public ContractorService() {
    }

    /**
     * Gets all contractors.
     *
     * @return list of contractorTO
     */
    public List<ContractorTO> getAll() {
        return ContractorTransformer.transformContractors(contractorDAO.getAll());
    }

    /**
     * Gets list of Contractor contacts by update id.
     *
     * @param id - update id
     * @return list of ContractorTO
     */
    public List<ContactTO> getContactsByContractorId(long id) {
        Contractor contractor = contractorDAO.get(id);
        return contractor == null ? new ArrayList<ContactTO>() : ContractorTransformer.transformContacts(contractor.getContacts());
    }

    public List<ShippingTO> getShippingsByContractorId(long id) {
        Contractor contractor = contractorDAO.get(id);
        return contractor == null ? new ArrayList<ShippingTO>() : ContractorTransformer.transformShippings(contractor.getShippings());
    }

    public Contractor getContractorByUid(String uid) {
        return contractorDAO.getContractorByUid(uid);
    }

    public List<String> getContractorUidsByIds(List<Long> ids) {
        return contractorDAO.getUidsByIds(ids);
    }

    /**
     * Updates passed contractorTO.
     *
     * @param contractorTO - ContractorTO  that needs to be updated
     */
    public void update(ContractorTO contractorTO) {
        Contractor contractor = contractorDAO.get(contractorTO.getId());
        Currency currency = currencyDAO.get(contractorTO.getDefaultCurrency().getId());
        ContractorTransformer.updateContractor(contractor, contractorTO, currency);
        contractorDAO.update(contractor);
    }

    /**
     * Updates datetime of request price list for the given contractors.
     *
     * @param contractorIDs IDs of the contractors for updating
     * @param priceListRequestDatetime New value of request price list
     */
    public void updatePriceListRequestDatetime(List<Long> contractorIDs, Date priceListRequestDatetime) {
        contractorDAO.updatePriceListRequestDatetime(contractorIDs, priceListRequestDatetime);
    }

    /**
     * Saves contractorTO.
     *
     * @param contractorTO - needs to be saved
     * @param contactTOs   - the list of just created contacts.
     * @param shippingTOs  - the list of just created shippings.
     */
    public void create(ContractorTO contractorTO, List<ContactTO> contactTOs, List<ShippingTO> shippingTOs) {
        Currency currency = currencyDAO.get(contractorTO.getDefaultCurrency().getId());
        Contractor persistentContractor;
        if (contractorTO.isNew()) {
            persistentContractor = new Contractor();
            ContractorTransformer.updateContractor(persistentContractor, contractorTO, currency);
            //Make accounts array for new (not saved) update
            for (Currency accountCurrency : currencyDAO.getAll()) {
                persistentContractor.getAccounts().add(new Account(accountCurrency, persistentContractor, new BigDecimal(0)));
            }
        } else {
            persistentContractor = contractorDAO.get(contractorTO.getId());
        }
        List<Contact> contacts = ContractorTransformer.transformAndUpdateContacts(contactTOs, persistentContractor);
        persistentContractor.setContacts(contacts);
        List<Shipping> shippings = ContractorTransformer.transformAndUpdateShippings(shippingTOs, persistentContractor);
        persistentContractor.setShippings(shippings);
        contractorDAO.save(persistentContractor);
        ContractorTransformer.updateContractor(contractorTO, persistentContractor);
    }

    /**
     * Gets list of accounts by update id.
     *
     * @param contractorId - update id
     * @return list of AccountTO
     */
    public List<AccountTO> getAccountsByContractorId(long contractorId) {
        Contractor contractor = contractorDAO.get(contractorId);
        List<Account> accounts = new ArrayList<Account>();
        if (contractor == null) {
            //Make accounts array for new (not saved) update
            for (Currency currency : currencyDAO.getAll()) {
                accounts.add(new Account(currency, contractor, new BigDecimal(0)));
            }
        } else {
            accounts = contractor.getAccounts();
        }
        return ContractorTransformer.trancformAccounts(accounts);
    }

    /**
     * Get account of the update in given currency.
     * @param contractorId
     * @param currencyId
     * @return
     */
    public AccountTO getAccountByContractorAndCurrency(long contractorId, long currencyId) {
        List<AccountTO> accounts = getAccountsByContractorId(contractorId);
        for (AccountTO account : accounts){
            if (account.getCurrency().getId() == currencyId){
                return account;
            }
        }
        return null;
    }


    /**
     * Gets update by id and delete it
     *
     * @param contractorTO - the update TO object
     */
    public void deleteContractor(ContractorTO contractorTO) {
        contractorDAO.remove(contractorDAO.get(contractorTO.getId()));
    }

    /**
     * Performs full contractor's update (contractor details, contacts, account balances) in one single transaction.
     * @param contractorTO edited contractor's data.
     * @param contactsTO edited contractor's contacts data.
     * @param accountActions actions, that have been performed with contractor's fifancial accounts.
     * @throws BusinessException
     */
    public void updateContractorFullData(ContractorTO contractorTO, List<ContactTO> contactsTO, List<ShippingTO> shippingTOs, List<AccountAction> accountActions) throws BusinessException{
        //1. Updating contractor's data.
        update(contractorTO);

        //2. Updating contacts and shippings data.
        updateContractorContactsAndShippings(contractorTO.getId(), contactsTO, shippingTOs);

        //3. Updating contractor's accounts.
        for (AccountAction action : accountActions){
            action.perform(accountService);
        }
    }

    /**
     * Updates update with new list of contacts.
     *
     * @param contractorId - the update id to what list of contacts should be updated
     * @param contactTOs   - list of items that should be set to update
     * @param shippingTOs  - list of items that should be set to update
     */
    public void updateContractorContactsAndShippings(long contractorId, List<ContactTO> contactTOs, List<ShippingTO> shippingTOs) {
        Contractor contractor = contractorDAO.get(contractorId);

        List<Contact> contacts = ContractorTransformer.transformAndUpdateContacts(contactTOs, contractor);
        for (Contact contact : contractor.getContacts()){
            if (!contacts.contains(contact)){
                contractorDAO.removeContact(contact);                
            }
        }
        contractor.setContacts(contacts);

        List<Shipping> shippings = ContractorTransformer.transformAndUpdateShippings(shippingTOs, contractor);
        for (Shipping shipping : contractor.getShippings()){
            if (!shippings.contains(shipping)){
                contractorDAO.removeShipping(shipping);
            }
        }
        contractor.setShippings(shippings);

        contractorDAO.save(contractor);
    }

    /**
     * Returns list of countries, available for contactors.
     *
     * @return
     */
    public List<String> getAllCountries() {
        List<String> countries = new ArrayList<String>();
        for (int i = 0; i < 260; ++i) {
            countries.add(I18nSupport.message("contractors.country" + i));
        }
        return countries;
    }

    /**
     * Gets all load places from DB where the load can be executed.
     *
     * @return - list of places where the load can be executed.
     */
    public List<LoadPlaceTO> getAllLoadPlaces() {
        return ContractorTransformer.loadPlacesList(loadPlaceDAO.getAllSortedByName());
    }


    /**
     * Saves a new load place.
     *
     * @param loadPlaceTO - a new load place;
     */
    public void saveLoadPlace(LoadPlaceTO loadPlaceTO) {
        LoadPlace loadPlace = new LoadPlace(loadPlaceTO.getId(), loadPlaceTO.getName(), loadPlaceTO.getDescription());
        loadPlaceDAO.save(loadPlace);
        loadPlaceTO.setId(loadPlace.getId());
    }

    /**
     * Checks, is the name of the load place will be unique.
     * @param name
     * @param loadPlaceId
     * @return
     */
    public boolean isUniqueLoadPlaceName(String name, long loadPlaceId){
        LoadPlace samePlace = loadPlaceDAO.getLoadPlaceByName(name);
        return samePlace == null || samePlace.getId() == loadPlaceId;
    }


    /**
     * Removes load place by it's id.
     *
     * @param loadPlaceTO - the load place that should be removed.
     */
    public void deleteLoadPlace(LoadPlaceTO loadPlaceTO) {
        loadPlaceDAO.removeById(loadPlaceTO.getId());
    }

    /**
     * Updates the  load place information
     *
     * @param loadPlaceTO - the updated loadp pace TO object
     */
    public void updateLoadPlace(LoadPlaceTO loadPlaceTO) {
        LoadPlace loadPlace = loadPlaceDAO.get(loadPlaceTO.getId());
        loadPlace.setName(loadPlaceTO.getName());
        loadPlace.setDescription(loadPlaceTO.getDescription());
        loadPlaceDAO.save(loadPlace);
    }


    public Contact getContactById(long contactId) {
        return this.contractorDAO.getContactById(contactId);
    }

    public Shipping getShippingById(long shippingId) {
        return this.contractorDAO.getShippingById(shippingId);
    }

    public Contractor getContractorById(long contractorId) {
        return contractorDAO.get(contractorId);
    }

    public LoadPlace getLoadPlaceById(long loadPlaceId) {
        return loadPlaceDAO.get(loadPlaceId);
    }

    /**
     * @return Contractors whose price lists were imported by system
     */
    public List<ContractorTO> getImportedPriceListsContractors() {
        // Get imports grouped by contractors
        List<ContractorPriceImport> priceImports = SpringServiceContext.getInstance()
                .getContractorPriceImportService().getImportsGroupedByContractors();

        // Select contractors
        List<ContractorTO> result = new ArrayList<ContractorTO>(priceImports.size());
        for (ContractorPriceImport priceImport : priceImports) {
            result.add(ContractorTransformer.transformContractor(priceImport.getContractor()));
        }
        return result;
    }

    public List<String> getAllUniqueAppointments() {
        return contactDAO.getAllUniqueAppointments();
    }

    public Contact getContractorContactByAppointment(long contractorId, String appointment) {
        Contractor contractor = getContractorById(contractorId);
        if (contractor == null){
            return null;
        }
        for (Contact contact : contractor.getContacts()){
            String contactAppointment = contact.getAppointment();
            if (!StringUtils.isStringNullOrEmpty(contactAppointment) && contactAppointment.equalsIgnoreCase(appointment)){
                return contact;
            }
        }
        return null;
    }

    public Contact getContractorContractById(long contactId) {
        return contactDAO.get(contactId);
    }

    //=======Spring setters and getters===========================
    public void setContractorDAO(ContractorDAOImpl contractorDAO) {
        this.contractorDAO = contractorDAO;
    }


    public void setCurrencyDAO(CurrencyDAOImpl currencyDAO) {
        this.currencyDAO = currencyDAO;
    }


    public void setLoadPlaceDAO(LoadPlaceDAOImpl loadPlaceDAO) {
        this.loadPlaceDAO = loadPlaceDAO;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setContactDAO(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }
}
