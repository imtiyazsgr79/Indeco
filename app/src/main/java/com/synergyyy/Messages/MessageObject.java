package com.synergyyy.Messages;

import java.util.List;

public class MessageObject {
    int count;
    public List<MessageResponse> messages;

    public MessageObject(int count, List<MessageResponse> messages) {
        this.count = count;
        this.messages = messages;
    }

    public int getCount() {
        return count;
    }

    public List<MessageResponse> getMessages() {
        return messages;
    }
}