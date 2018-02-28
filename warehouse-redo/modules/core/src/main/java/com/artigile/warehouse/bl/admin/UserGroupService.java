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

import com.artigile.warehouse.dao.UserGroupDAO;
import com.artigile.warehouse.dao.UserPermissionDAO;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.admin.UserGroup;
import com.artigile.warehouse.domain.admin.UserPermission;
import com.artigile.warehouse.utils.dto.UserGroupTO;
import com.artigile.warehouse.utils.dto.UserPermissionTO;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Shyrik, 07.12.2008
 */
@Transactional
public class UserGroupService {

    private UserGroupDAO userGroupDAO;

    private UserPermissionDAO userPermissionDAO;

    /**
     * List of permissions, that administrators group should have.
     */
    private List<UserPermissionTO> adminPermissionsTO;

    public UserGroupService() {
    }

    /**
     * @return - List of all groups of users.
     */
    public Set<UserGroupTO> getAllGroups() {
        return UserTransformer.transformGroups(new HashSet<UserGroup>(userGroupDAO.getAll()));
    }

    /**
     * Gets group by name.
     *
     * @param groupName - the name of the group
     * @return -group from object
     */
    public UserGroup getGroupByName(String groupName) {
        return userGroupDAO.getGroupByName(groupName);
    }

    /**
     * Updates user group.
     *
     * @param groupTO group data
     * @param permissions - list of permissions assigned to the group.
     * @return id of saved user group
     */
    public long saveOrUpdateGroup(UserGroupTO groupTO, List<UserPermissionTO> permissions) {
        UserGroup userGroup;
        if (groupTO.isNew()) {
            userGroup = new UserGroup();
        } else {
            userGroup = userGroupDAO.get(groupTO.getId());
        }
        List<UserPermission> userPermissions = new ArrayList<UserPermission>();
        for (UserPermissionTO permission : permissions) {
            userPermissions.add(userPermissionDAO.get(permission.getId()));
        }
        userGroup.setName(groupTO.getName());
        userGroup.setDescription(groupTO.getDescription());
        userGroup.setPermissions(userPermissions);
        userGroupDAO.saveOrUpdate(userGroup);
        return userGroup.getId();
    }

    /**
     * Deletes user group.
     *
     * @param groupTO
     */
    public void deleteGroup(UserGroupTO groupTO) {
        userGroupDAO.remove(userGroupDAO.get(groupTO.getId()));
    }

    /**
     * Returns a list of permissions set for given user group.
     * @param groupId
     * @return
     */
    public List<UserPermissionTO> getPermissionByGroupId(long groupId) {
        UserGroup group = userGroupDAO.get(groupId);
        if (group == null){
            return new ArrayList<UserPermissionTO>();
        }
        return UserTransformer.transformPermissions(group.getPermissions());
    }

    public UserGroup getGroupById(long groupId) {
        return userGroupDAO.get(groupId);
    }

    /**
     * Returns a list of permissions, that administrators group should have.
     * @return
     */
    public List<UserPermissionTO> getAdminPermissions() {
        if (adminPermissionsTO == null){
            List<UserPermission> adminPermissions = new ArrayList<UserPermission>();
            adminPermissions.add(userPermissionDAO.getPermissionByType(PermissionType.VIEW_USERS));
            adminPermissions.add(userPermissionDAO.getPermissionByType(PermissionType.EDIT_USERS));
            adminPermissions.add(userPermissionDAO.getPermissionByType(PermissionType.VIEW_GROUPS));
            adminPermissions.add(userPermissionDAO.getPermissionByType(PermissionType.EDIT_GROUPS));
            adminPermissions.add(userPermissionDAO.getPermissionByType(PermissionType.VIEW_CHANGE_PASS));
            adminPermissions.add(userPermissionDAO.getPermissionByType(PermissionType.VIEW_LICENSES));
            adminPermissions.add(userPermissionDAO.getPermissionByType(PermissionType.EDIT_LICENSES));
            adminPermissions.add(userPermissionDAO.getPermissionByType(PermissionType.VIEW_HARDWARE_ID));
            adminPermissionsTO = UserTransformer.transformPermissions(adminPermissions);
        }
        return adminPermissionsTO;
    }

    //================== Spring setters =====================
    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public void setUserPermissionDAO(UserPermissionDAO userPermissionDAO) {
        this.userPermissionDAO = userPermissionDAO;
    }
}
