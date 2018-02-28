/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.admin.UserGroup;
import com.artigile.warehouse.domain.admin.UserPermission;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserGroupTO;
import com.artigile.warehouse.utils.dto.UserPermissionTO;
import com.artigile.warehouse.utils.dto.UserTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Transforms user related objects.
 *
 * @author IoaN, Dec 10, 2008
 */
public final class UserTransformer {
    private UserTransformer() {
    }

    /**
     * Transforms list of UserTO  into list of User.
     *
     * @param users - list of domain objects
     * @return list of TO objects
     */
    public static List<UserTO> transformUsers(List<User> users) {
        List<UserTO> newList = new ArrayList<UserTO>();
        for (User user : users) {
            newList.add(transformUser(user));
        }
        return newList;
    }

    /**
     * Transforms User into UserTO.
     *
     * @param user - domain object
     * @return TO object
     */
    public static UserTO transformUser(User user) {
        if (user == null) {
            return null;
        }

        UserTO userTO = new UserTO();
        update(userTO, user);
        return userTO;
    }

    /**
     * Transofms UserTO into User.
     *
     * @param userTO - TO object
     * @return domain object
     */
    public static User transformUser(UserTO userTO) {
        if (userTO == null) {
            return null;
        }
        User user = SpringServiceContext.getInstance().getUserService().getUserById(userTO.getId());
        if (user == null) {
            user = new User();
        }
        return user;
    }

     /**
     * @param userTO (in, out) - DTO to be updated.
     * @param user (in) - entity with fresh data.
     */
    public static void update(UserTO userTO, User user) {
        userTO.setId(user.getId());
        userTO.setPredefined(user.isPredefined());
        userTO.setLogin(user.getLogin());
        userTO.setPassword(user.getPassword());
        userTO.setFirstName(user.getFirstName());
        userTO.setLastName(user.getLastName());
        userTO.setEmail(user.getEmail());
        userTO.setNameOnProduct(user.getNameOnProduct());
        userTO.setSimplifiedWorkplace(user.getSimplifiedWorkplace());
    }

    /**
     * @param user (in, out) - entity to be syncronized with DTO.
     * @param userTO (in) - DTO with fresh data.
     */
    public static void update(User user, UserTO userTO) {
        user.setLogin(userTO.getLogin());
        user.setFirstName(userTO.getFirstName());
        user.setLastName(userTO.getLastName());
        user.setEmail(userTO.getEmail());
        user.setPassword(userTO.getPassword());
        user.setPredefined(userTO.isPredefined());
        user.setNameOnProduct(userTO.getNameOnProduct());
        user.setSimplifiedWorkplace(userTO.getSimplifiedWorkplace());
    }

    /**                                         
     * Transforms list of UserPermission into list of UserPermissionTO.
     *
     * @param userPermissions - list of domain objects
     * @return list of TO objects
     */
    public static List<UserPermissionTO> transformPermissions(List<UserPermission> userPermissions) {
        List<UserPermissionTO> userPermissionTOs = new ArrayList<UserPermissionTO>();
        for (UserPermission userPermission : userPermissions) {
            UserPermissionTO userPermissionTO = new UserPermissionTO();
            userPermissionTO.setId(userPermission.getId());
            userPermissionTO.setName(userPermission.getName());
            userPermissionTO.setDescription(userPermission.getDescription());
            userPermissionTOs.add(userPermissionTO);
        }
        return userPermissionTOs;
    }

    /**
     * Transforms set of UserGroup into set of UserGroupTO.
     *
     * @param userGroups - set of domain objects
     * @return set of TO objects
     */
    public static Set<UserGroupTO> transformGroups(Set<UserGroup> userGroups) {
        Set<UserGroupTO> newList = new HashSet<UserGroupTO>();
        for (UserGroup userGroup : userGroups) {
            newList.add(transformGroup(userGroup));
        }
        return newList;
    }

    /**
     * Transforms UserGroup into UserGroupTO.
     *
     * @param userGroup - domain object
     * @return TO object
     */
    public static UserGroupTO transformGroup(UserGroup userGroup) {
        if (userGroup == null){
            return null;
        }
        UserGroupTO userGroupTO = new UserGroupTO();
        update(userGroupTO, userGroup);
        return userGroupTO;
    }

    private static void update(UserGroupTO userGroupTO, UserGroup userGroup) {
        userGroupTO.setId(userGroup.getId());
        userGroupTO.setPredefined(userGroup.isPredefined());
        userGroupTO.setName(userGroup.getName());
        userGroupTO.setDescription(userGroup.getDescription());
    }
}
