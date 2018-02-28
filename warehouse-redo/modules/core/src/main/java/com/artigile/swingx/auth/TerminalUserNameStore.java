package com.artigile.swingx.auth;

import com.artigile.warehouse.utils.authentification.MySqlAuthenticator;
import org.jdesktop.swingx.auth.UserNameStore;

import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Valery Barysok, 2013-07-23
 */
public class TerminalUserNameStore extends UserNameStore {

    private static final String USER = "userlist";

    private static final String PASSWORD = "{61B3E12B-3586-3A58-A497-7ED7C4C794B9}";

    private PropertyChangeSupport pcs;

    private String[] userNames;

    public TerminalUserNameStore() {
        pcs = new PropertyChangeSupport(this);
        userNames = new String[0];
    }

    @Override
    public String[] getUserNames() {
        String[] copy = new String[userNames.length];
        System.arraycopy(userNames, 0, copy, 0, userNames.length);
        return copy;
    }

    @Override
    public void setUserNames(String[] names) {
        if (userNames != names) {
            String[] old = userNames;
            userNames = names == null ? new String[0] : names;
            pcs.firePropertyChange("userNames", old, userNames);
        }
    }

    @Override
    public void loadUserNames() {
        try {
            Connection connection = MySqlAuthenticator.getConnection(USER, PASSWORD);
            ResultSet resultSet = connection.prepareStatement("select * from user_list").executeQuery();
            List<String> users = new ArrayList<String>();
            while (resultSet.next()) {
                users.add(resultSet.getString(1));
            }
            setUserNames(users.toArray(new String[0]));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUserNames() {
    }

    @Override
    public boolean containsUserName(String name) {
        return Arrays.binarySearch(getUserNames(), name) >= 0;
    }

    @Override
    public void addUserName(String userName) {
    }

    @Override
    public void removeUserName(String userName) {
    }
}
