package com.ts.learning.sws101.model.simplechat;

import com.ts.learning.sws101.model.simplechat.server.AbstractServerMessage;

public class SimpleFromMessage extends AbstractServerMessage {
    public static final String TYPE = "FROM_MSG";

    private String fromUser;
    private String message;

    public SimpleFromMessage() {
    }

    public SimpleFromMessage(String fromUser, String message) {
        this.fromUser = fromUser;
        this.message = message;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
