package com.ts.learning.sws101.model.simplechat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ActiveUsersListMessage {
    public static final String TYPE = "TYPE_ACTIVE_USERS";

    public List<String> activeUsers;

    public ActiveUsersListMessage() {
    }

    public ActiveUsersListMessage(Collection<String> activeUsers) {
        this.activeUsers = new LinkedList<>(activeUsers);
    }

    public List<String> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(List<String> activeUsers) {
        this.activeUsers = activeUsers;
    }
}
