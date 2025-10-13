package com.mycompany.javapoepart1;

import javax.swing.JOptionPane;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;

public class Message {
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private static int totalMessages = 0;

    // Constructor initializes message fields and generates hash.
    public Message(String recipient, String messageText, int messageNumber) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash(messageID, messageNumber, messageText);
    }

    // Genrate random message ID
    private String generateMessageID() {
        Random rand = new Random();
        return String.format("%010d", rand.nextInt(1_000_000_000));
    }

    // Validates that the message ID is exactly 10 digits.
    public boolean checkMessageID() {
        return messageID.length() == 10;
    }

    
   // Validates recipient format: starts with +27 and followed by exactly 9 digits
        public int checkRecipientCell() {
            if (recipient.startsWith("+27") && recipient.length() == 12 && recipient.substring(3).matches("\\d{9}")) {
                System.out.println("Cell phone number successfully captured.");
                return 1;
            } else {
                System.out.println("Cell phone number is incorrectly formatted.");
                return 0;
            }
    }

    // Message length <= 250
    public int checkMessageLength() {
        if (messageText.length() <= 250) {
            System.out.println("Message ready to send.");
            return 1;
        } else {
            int excess = messageText.length() - 250;
            System.out.println("Message exceeds 250 characters by " + excess + ". Please reduce size.");
            return 0;
        }
    }

    // Creates a message hash using string manipulation.
    public String createMessageHash(String id, int num, String msg) {
        String[] words = msg.trim().split(" ");
        String first = words.length > 0 ? words[0] : "";
        String last = words.length > 1 ? words[words.length - 1] : first;
        String hash = id.substring(0, 2) + ":" + String.format("%02d", num) + first + last;
        return hash.toUpperCase();
    }

    // Prompt the user to choose what to do with the message
    public String SentMessage() {
        String input = JOptionPane.showInputDialog("Choose:\n1) Send Message\n2) Disregard Message\n3) Store Message");
        switch (input) {
            case "1":
                totalMessages++;
                return "Send";
            case "2":
                return "Disregard";
            case "3":
                return "Store";
            default:
                return "Invalid";
        }
    }

    // Returns message details to display
    public String getDetails() {
        return "Message ID: " + messageID +
               "\nMessage Hash: " + messageHash +
               "\nRecipient: " + recipient +
               "\nMessage: " + messageText;
    }

    // Returns total number of messages sent.
    public static int returnTotalMessages() {
        return totalMessages;
    }

    // Stores message in a JSON file using Gson. Used ChatGPT for the storing as indacated in the rubric
    public void storeMessage() {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter("storedMessage.json", true);
            gson.toJson(this, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
}
