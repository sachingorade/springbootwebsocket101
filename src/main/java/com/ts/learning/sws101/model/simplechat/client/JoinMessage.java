package com.ts.learning.sws101.model.simplechat.client;

public class JoinMessage extends AbstractClientMessage {

    public static final String TYPE = "JOIN";

    private String userName;

    public JoinMessage() {
        this(null);
    }

    public JoinMessage(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
