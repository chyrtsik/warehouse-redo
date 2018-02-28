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

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.license.LicenseService;
import com.artigile.warehouse.dao.*;
import com.artigile.warehouse.domain.MenuItem;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import com.artigile.warehouse.utils.authentification.MySqlAuthenticator;
import com.artigile.warehouse.utils.dto.UserGroupTO;
import com.artigile.warehouse.utils.dto.UserPermissionTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import com.artigile.warehouse.utils.transofmers.WarehouseTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * User Service class.
 *
 * @author Ihar, Nov 29, 2008
 */
@Transactional(rollbackFor = BusinessException.class)
public class UserService {
    private final String VALID_DB_LOGIN_PASSWORD = "^[a-zA-Z0-9_]{4,}$";

    private UserDAO userDAO;

    private MenuItemDAO menuItemDAO;

    private UserGroupDAO userGroupDAO;

    private UserPermissionDAO userPermissionDAO;

    private WarehouseDAO warehouseDAO;

    private LicenseService licenseService;

    public UserService() {
    }

    public User getUserById(long userId) {
        return userDAO.get(userId);
    }

    public UserTO getUserByLogin(String login) {
        User user = userDAO.getUserByLogin(login);
        return user == null ? null : UserTransformer.transformUser(user);
    }

    public UserTO getUserByNameOnProduct(String nameOnProduct) {
        User user = userDAO.getUserByNameOnProduct(nameOnProduct);
        return user == null ? null : UserTransformer.transformUser(user);
    }

    public void updatePassword(Long userId, String newPassword) throws BusinessException {
        if (!isValidUserPassword(newPassword)) {
            throw new BusinessException(I18nSupport.message("user.validation.bad.password"));
        }
        User user = userDAO.get(userId);

        // TODO: When updating current user we should also update:
        // TODO: connectionProperties.setProperty("user", dbLogin);
        // TODO: connectionProperties.setProperty("password", dbPassword);
        if (MySqlAuthenticator.updatePassword(user.getLogin(), newPassword)) {
            user.setPassword(MySqlAuthenticator.encodePassword(newPassword));
            userDAO.update(user);
            userDAO.flush();
        }
        else{
            throw new BusinessException(I18nSupport.message("user.error.cannotUpdateDatabaseUserPassword"));
        }
    }

    public void saveUser(UserTO userTO, Set<UserGroupTO> groups, List<WarehouseTOForReport> warehouses) throws BusinessException {
        if (!isValidUserLogin(userTO.getLogin())){
            throw new BusinessException(I18nSupport.message("user.validation.bad.login"));
        }
        else if (!isValidUserPassword(userTO.getPassword())){
            throw new BusinessException(I18nSupport.message("user.validation.bad.password"));
        }

        User persistentUser = userDAO.get(userTO.getId());
        if (persistentUser == null) {
            //Creating new user.
            persistentUser = new User();
            UserTransformer.update(persistentUser, userTO);
            persistentUser.setPassword(MySqlAuthenticator.encodePassword(userTO.getPassword()));
            addGroupsToUser(persistentUser, groups);
            addWarehousesToUser(persistentUser, warehouses);
            userDAO.save(persistentUser);
            userDAO.flush();
            if (!MySqlAuthenticator.createNewUser(userTO.getLogin(), userTO.getPassword())) {
                throw new BusinessException(I18nSupport.message("user.error.cannotCreateDatabaseUser"));
            }
        } else {
            //Update existing user.
            String prevLogin = persistentUser.getLogin();
            UserTransformer.update(persistentUser, userTO);
            addGroupsToUser(persistentUser, groups);
            addWarehousesToUser(persistentUser, warehouses);
            userDAO.update(persistentUser);
            userDAO.flush();
            if (!prevLogin.equals(persistentUser.getLogin())){
                if (!MySqlAuthenticator.updateUserLogin(prevLogin, userTO.getLogin())) {
                    throw new BusinessException(I18nSupport.message("user.error.cannotUpdateDatabaseUser"));
                }
            }
        }
        UserTransformer.update(userTO, persistentUser);
    }

    public void deleteUser(UserTO user) throws BusinessException {
        userDAO.remove(userDAO.get(user.getId()));
        userDAO.flush();
        if (!MySqlAuthenticator.removeUser(user.getLogin())) {
            throw new BusinessException(I18nSupport.message("user.error.cannotDeleteDatabaseUser"));
        }
    }

    public boolean checkPermission(long userId, PermissionType permission) {
        //TODO: Temporary disabled permissions check. Will enable when working on optimization.
        //return checkPermission(userDAO.get(userId), permission);
        return true;
    }

    private boolean checkPermission(User user, PermissionType permission) {
        //TODO: Temporary disabled permissions check. Will enable when working on optimization.
        //return user.hasRight(permission) && licenseService.checkPermission(user, permission);
        return true;
    }

    public List<UserTO> getAllUsers() {
        return UserTransformer.transformUsers(userDAO.getAll());
    }

    public Set<UserGroupTO> getGroupsByUserId(long id) {
        User user = userDAO.get(id);
        return user == null ? new HashSet<UserGroupTO>() : UserTransformer.transformGroups(user.getGroups());
    }

    public List<WarehouseTOForReport> getUserMayComplectWarehouses(long userId) {
        User user = userDAO.get(userId);
        if (user != null) {
            return WarehouseTransformer.transformListForReport(user.getWarehouses());
        } else {
            return Collections.<WarehouseTOForReport>emptyList();
        }
    }

    /**
     * Loads menu items, available for given user.
     *
     * @param userId
     * @return
     */
    public List<MenuItem> loadMenuItemsForUser(long userId) {
        User user = userDAO.get(userId);
        List<MenuItem> allMenuItems = menuItemDAO.getAllSortedByName();
        List<MenuItem> availableMenuItems = new ArrayList<MenuItem>();
        for (MenuItem menuItem : allMenuItems) {
            if (menuItem.getViewPermission() == null || checkPermission(user, menuItem.getViewPermission().getRightType())) {
                availableMenuItems.add(menuItem);
            }
        }
        return availableMenuItems;
    }

    /**
     * @return - List of all rights, that are supported by the system.
     */
    public List<UserPermissionTO> getAllPermissions() {
        return UserTransformer.transformPermissions(userPermissionDAO.getAll());
    }

    /**
     * Gets all users, that are allowed to complect parcels from warehouse.
     *
     * @param warehouseId
     * @return
     */
    public List<UserTO> getUsersForWarehouse(long warehouseId) {
        return UserTransformer.transformUsers(userDAO.getUsersForWarehouse(warehouseDAO.get(warehouseId)));
    }

    /**
     * Saving  users, that are allowed to complect parcels from the specified warehouse.
     *
     * @param warehouseId
     * @param allowedUsers
     */
    public void saveUsersForWarehouse(long warehouseId, List<UserTO> allowedUsers) {
        Warehouse warehouse = warehouseDAO.get(warehouseId);
        List<User> oldUsers = userDAO.getUsersForWarehouse(warehouseDAO.get(warehouseId));
        List<UserTO> newUsers = new ArrayList<UserTO>(allowedUsers);

        //1. Finding users, those were not affected by new changes.
        for (int i = allowedUsers.size() - 1; i >= 0; i--) {
            for (User user : oldUsers) {
                if (user.getId() == allowedUsers.get(i).getId()) {
                    oldUsers.remove(user);
                    newUsers.remove(i);
                    break;
                }
            }
        }

        //2. Deleting links for users, those are not allowed to work with warehouse (but were allowed earlier).
        for (User user : oldUsers) {
            user.deleteFromWarehouse(warehouse);
            userDAO.save(user);
        }

        //3. Creating links for new users, allowed to work with warehouse.
        for (UserTO newUser : newUsers) {
            User user = userDAO.get(newUser.getId());
            user.addToWarehouse(warehouse);
            userDAO.save(user);
        }
    }

    private void addGroupsToUser(User user, Set<UserGroupTO> groups) {
        user.getGroups().clear();
        for (UserGroupTO group : groups) {
            user.addToGroups(userGroupDAO.get(group.getId()));
        }
    }

    private void addWarehousesToUser(User user, List<WarehouseTOForReport> warehouses) {
        user.getWarehouses().clear();
        for (WarehouseTOForReport warehouse : warehouses) {
            user.addToWarehouse(warehouseDAO.get(warehouse.getId()));
        }
    }

    /**
     * Checks, if given string is a valid login value.
     * @param login
     * @return
     */
    public boolean isValidUserLogin(String login) {
        return login.matches(VALID_DB_LOGIN_PASSWORD);
    }

    /**
     * Checks, if given string is a valid password value.
     * @param password
     * @return
     */
    public boolean isValidUserPassword(String password) {
        return password.matches(VALID_DB_LOGIN_PASSWORD);
    }

    //==============Spring setters=============
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setMenuItemDAO(MenuItemDAO menuItemDAO) {
        this.menuItemDAO = menuItemDAO;
    }

    public void setUserGroupDAO(UserGroupDAOImpl userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public void setUserPermissionDAO(UserPermissionDAO userPermissionDAO) {
        this.userPermissionDAO = userPermissionDAO;
    }

    public void setWarehouseDAO(WarehouseDAO warehouseDAO) {
        this.warehouseDAO = warehouseDAO;
    }

    public void setLicenseService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }
}
