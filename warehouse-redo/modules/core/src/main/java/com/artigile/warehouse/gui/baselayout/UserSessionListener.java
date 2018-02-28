package com.artigile.warehouse.gui.baselayout;

/**
 * Interface for listeners of user session related events (ex., user logged in event).
 *
 * @author Aliaksandr.Chyrtsik, 10.07.11
 */
public interface UserSessionListener {
    /**
     * Called when user begins his session with application (after logging in).
     * @param userSessionInfo information about new user session.
     */
    void onUserSessionBegin(UserSessionInfo userSessionInfo);
}
