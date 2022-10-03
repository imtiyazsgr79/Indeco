package com.synergyyy.Messages;

public class MessageCardDetails {
    String messageTitle;
    String messageText;
    String messageTime;
    String messageType;
    Integer id;
    Boolean seen;
    String token;

    public MessageCardDetails(String messageTitle, String messageText, String messageTime, String messageType,Integer id,Boolean seen,String token) {
        this.messageTitle = messageTitle;
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.messageType = messageType;
        this.id=id;
        this.seen=seen;
        this.token=token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
