// Updated Message.java
package com.mycompany.poepartone;

import java.io.Serializable;

public class Message implements Serializable {
    private static int counter = 0;

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;

    // Default constructor for JSON deserialization
    public Message() {}

    public Message(String recipient, String messageText) {
        counter++;
        this.messageID = String.format("%010d", counter);
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public boolean checkRecipientCell() {
        return recipient != null && recipient.matches("^\\+\\d{10,12}$");
    }

    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : "";
        return messageID.substring(0, 2) + ":" + counter + ":" + firstWord.toUpperCase() + lastWord.toUpperCase();
    }

    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String printMessage() {
        return "Message ID: " + messageID +
               "\nMessage Hash: " + messageHash +
               "\nRecipient: " + recipient +
               "\nMessage: " + messageText;
    }
} 
