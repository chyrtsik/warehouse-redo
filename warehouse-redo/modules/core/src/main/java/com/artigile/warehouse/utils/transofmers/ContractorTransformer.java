/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.domain.contractors.Contact;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.contractors.LoadPlace;
import com.artigile.warehouse.domain.contractors.Shipping;
import com.artigile.warehouse.domain.finance.Account;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ihar, Dec 10, 2008
 */

public final class ContractorTransformer {
    private ContractorTransformer() {
    }

    private static ContractorService getContractorService(){
        return SpringServiceContext.getInstance().getContractorService();
    }

    /**
     * Transforms list of Contractor into list of ContractorTO.
     *
     * @param contractors - list of domain objects
     * @return list of TO objects
     */
    public static List<ContractorTO> transformContractors(List<Contractor> contractors) {
        List<ContractorTO> newList = new ArrayList<ContractorTO>();
        for (Contractor contractor : contractors) {
            newList.add(transformContractor(contractor));
        }
        return newList;

    }

    /**
     * Transofms Contractor into ContractorTO.
     * @param contractorTO (out)
     * @param contractor (in)
     */
    public static void updateContractor(ContractorTO contractorTO, Contractor contractor) {
        contractorTO.setId(contractor.getId());
        contractorTO.setUidContractor(contractor.getUidContractor());
        contractorTO.setName(contractor.getName());
        contractorTO.setFullName(contractor.getFullname());
        contractorTO.setRating(contractor.getRating());
        contractorTO.setLegalAddress(contractor.getLegalAddress());
        contractorTO.setPostalAddress(contractor.getPostalAddress());
        contractorTO.setPhone(contractor.getPhone());
        contractorTO.setDefaultShippingAddress(transformLoadPlace(contractor.getDefaultShippingAddress()));
        contractorTO.setCountry(contractor.getCountry());
        contractorTO.setDefaultCurrency(CurrencyTransformer.transformCurrency(contractor.getDefaultCurrency()));
        contractorTO.setWebSiteURL(contractor.getWebSiteURL());
        contractorTO.setEmail(contractor.getEmail());
        contractorTO.setBankAccount(contractor.getBankAccount());
        contractorTO.setBankFullData(contractor.getBankFullData());
        contractorTO.setBankShortData(contractor.getBankShortData());
        contractorTO.setBankCode(contractor.getBankCode());
        contractorTO.setBankAddress(contractor.getBankAddress());
        contractorTO.setOkpo(contractor.getOkpo());         
        contractorTO.setUnp(contractor.getUnp());
        contractorTO.setNotice(contractor.getNotice());
        contractorTO.setDiscount(contractor.getDiscount());
        contractorTO.setPriceListRequest(contractor.getPriceListRequest());
    }

    public static ContractorTO transformContractor(Contractor contractor) {
        if (contractor == null){
            return null;
        }

        ContractorTO contractorTO = new ContractorTO();
        updateContractor(contractorTO, contractor);
        return contractorTO;
    }

    /**
     * Fill entity from updated DTO.
     *
     * @param contractor   - (out) entity
     * @param contractorTO - (in) update DTO, that has come from UI
     * @param currency     - (in) default currency of update
     */
    public static void updateContractor(Contractor contractor, ContractorTO contractorTO, Currency currency) {
        contractor.setName(contractorTO.getName());
        contractor.setFullname(contractorTO.getFullName());
        contractor.setRating(contractorTO.getRating());
        contractor.setLegalAddress(contractorTO.getLegalAddress());
        contractor.setPostalAddress(contractorTO.getPostalAddress());
        contractor.setPhone(contractorTO.getPhone());
        contractor.setBankAccount(contractorTO.getBankAccount());
        contractor.setBankFullData(contractorTO.getBankFullData());
        contractor.setBankShortData(contractorTO.getBankShortData());
        contractor.setDefaultCurrency(currency);
        contractor.setDefaultShippingAddress(transformLoadPlace(contractorTO.getDefaultShippingAddress()));
        contractor.setCountry(contractorTO.getCountry());
        contractor.setBankCode(contractorTO.getBankCode());
        contractor.setBankAddress(contractorTO.getBankAddress());
        contractor.setOkpo(contractorTO.getOkpo());
        contractor.setUnp(contractorTO.getUnp());
        contractor.setWebSiteURL(contractorTO.getWebSiteURL());
        contractor.setEmail(contractorTO.getEmail());
        contractor.setNotice(contractorTO.getNotice());
        contractor.setDiscount(contractorTO.getDiscount());
        contractor.setPriceListRequest(contractorTO.getPriceListRequest());
    }

    public static Contractor transformContractor(ContractorTO contractorTO) {
        if (contractorTO == null){
            return null;
        }

        Contractor contractorEntity = getContractorService().getContractorById(contractorTO.getId());
        if (contractorEntity == null){
            contractorEntity = new Contractor();
        }

        return contractorEntity;
    }

    public static List<Contact> transformAndUpdateContacts(List<ContactTO> contactTOs, Contractor contractor) {
        List<Contact> contacts = new ArrayList<Contact>();
        for (ContactTO contactTO : contactTOs) {
            Contact contact = transformContact(contactTO);
            updateContact(contact, contactTO);
            contact.setContractor(contractor);
            contacts.add(contact);
        }
        return contacts;
    }

    public static List<ContactTO> transformContacts(List<Contact> contacts) {
        List<ContactTO> newList = new ArrayList<ContactTO>();
        for (Contact contact : contacts) {
            newList.add(transformContact(contact));
        }
        return newList;
    }

    public static ContactTO transformContact(Contact contact) {
        if (contact == null){
            return null;
        }
        return new ContactTO(contact.getId(), contact.getFullName(), contact.getAppointment(), contact.getPhone(), contact.getEmail(), contact.getIcqId(), contact.getSkypeId(), contact.getNotice());
    }

    public static Contact transformContact(ContactTO contactTO) {
        Contact contact = getContractorService().getContactById(contactTO.getId());
        if (contact == null){
            contact = new Contact();
        }
        return contact;
    }

    private static void updateContact(Contact contact, ContactTO contactTO) {
        contact.setEmail(contactTO.getEmail());
        contact.setIcqId(contactTO.getIcqId());
        contact.setFullName(contactTO.getFullName());
        contact.setAppointment(contactTO.getAppointment());
        contact.setNotice(contactTO.getNotice());
        contact.setPhone(contactTO.getPhone());
        contact.setSkypeId(contactTO.getSkypeId());
    }

    public static List<Shipping> transformAndUpdateShippings(List<ShippingTO> shippingTOs, Contractor contractor) {
        List<Shipping> shippings = new ArrayList<Shipping>();
        for (ShippingTO shippingTO : shippingTOs) {
            Shipping shipping = transformShipping(shippingTO);
            updateShipping(shipping, shippingTO);
            shipping.setContractor(contractor);
            shippings.add(shipping);
        }
        return shippings;
    }

    public static List<ShippingTO> transformShippings(List<Shipping> shippings) {
        List<ShippingTO> newList = new ArrayList<ShippingTO>();
        for (Shipping shipping : shippings) {
            newList.add(transformShipping(shipping));
        }
        return newList;
    }

    public static ShippingTO transformShipping(Shipping shipping) {
        return new ShippingTO(shipping.getId(), shipping.getAddress(), shipping.getCourier(), shipping.getPhone(), shipping.getNote());
    }

    private static Shipping transformShipping(ShippingTO shippingTO) {
        Shipping shipping = getContractorService().getShippingById(shippingTO.getId());
        if (shipping == null){
            shipping = new Shipping();
        }
        return shipping;
    }

    private static void updateShipping(Shipping shipping, ShippingTO shippingTO) {
        shipping.setAddress(shippingTO.getAddress());
        shipping.setCourier(shippingTO.getCourier());
        shipping.setPhone(shippingTO.getPhone());
        shipping.setNote(shippingTO.getNote());
    }

    public static List<AccountTO> trancformAccounts(List<Account> accounts) {
        List<AccountTO> newList = new ArrayList<AccountTO>();
        for (Account account : accounts) {
            newList.add(transformAccount(account));
        }
        return newList;
    }

    public static AccountTO transformAccount(Account account) {
        return new AccountTO(account.getId(), CurrencyTransformer.transformCurrency(account.getCurrency()), account.getCurrentBalance());
    }

    public static List<LoadPlaceTO> loadPlacesList(List<LoadPlace> loadPlaces) {
        List<LoadPlaceTO> loadPlaceTOs = new ArrayList<LoadPlaceTO>();
        for (LoadPlace loadPlace : loadPlaces) {
            loadPlaceTOs.add(transformLoadPlace(loadPlace));
        }
        return loadPlaceTOs;
    }

    public static LoadPlaceTO transformLoadPlace(LoadPlace loadPlace) {
        if (loadPlace == null){
            return null;
        }
        return new LoadPlaceTO(loadPlace.getId(), loadPlace.getName(), loadPlace.getDescription());
    }

    public static LoadPlace transformLoadPlace(LoadPlaceTO loadPlaceTO) {
        if (loadPlaceTO == null){
            return null;
        }
        LoadPlace loadPlace = SpringServiceContext.getInstance().getContractorService().getLoadPlaceById(loadPlaceTO.getId());
        if (loadPlace == null){
            loadPlace = new LoadPlace(loadPlaceTO.getId(), loadPlaceTO.getName(), loadPlaceTO.getDescription());
        }
        return loadPlace;
    }
}
