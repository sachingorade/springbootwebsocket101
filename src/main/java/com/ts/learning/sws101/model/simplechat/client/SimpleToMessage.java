package com.ts.learning.sws101.model.simplechat.client;

public class SimpleToMessage extends AbstractClientMessage {

    public static final String TYPE = "TO_MSG";

    private String toUser;
    private String message;

    public SimpleToMessage() {
    }

    public SimpleToMessage(String toUser, String message) {
        this.toUser = toUser;
        this.message = message;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
