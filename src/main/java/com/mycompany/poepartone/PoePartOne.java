// Updated PoePartOne.java with high-school-level comments
package com.mycompany.poepartone;
//final  poe 
import javax.swing.*;
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// This is the main class where the chat app starts and runs
public class PoePartOne {
    // Variables to store the user's login details
    private String username;
    private String password;
    private String cellPhoneNumber;

    // Arrays to keep track of different types of messages
    ArrayList<Message> sentMessages = new ArrayList<>(); // messages that were sent
    private ArrayList<Message> disregardedMessages = new ArrayList<>(); // messages skipped
    private ArrayList<Message> storedMessages = new ArrayList<>(); // messages saved for later
    private ArrayList<String> messageHashes = new ArrayList<>(); // stores hash codes of messages
    private ArrayList<String> messageIDs = new ArrayList<>(); // stores message IDs

    // Allows test files to access sent messages
    public List<Message> getSentMessages() {
        return sentMessages;
    }

    // File where stored messages will be saved as JSON
    private final String JSON_FILE = "storedMessages.json";

    // Checks if username is valid (must have an underscore and max 5 characters)
    public boolean checkUserName(String username) {
        return username != null && !username.isEmpty() && username.contains("_") && username.length() <= 5;
    }

    // Checks if password is strong (has capital letter, number, special character, min 8 chars)
    public boolean checkPasswordComplexity(String password) {
        return password != null &&
                password.length() >= 8 &&
                Pattern.compile("[A-Z]").matcher(password).find() &&
                Pattern.compile("[0-9]").matcher(password).find() &&
                Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
    }

    // Checks if phone number is correctly formatted with +27 and 9 digits
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        return cellPhoneNumber != null && !cellPhoneNumber.isEmpty() &&
                Pattern.matches("^\\+27\\d{9}$", cellPhoneNumber);
    }

    // This method handles user registration and validation
    public String registerUser(String username, String password, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    // This is the flow for sending messages
    public void sendMessageFlow() {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");
        int numMessages = Integer.parseInt(JOptionPane.showInputDialog("How many messages do you want to send?"));

        for (int i = 0; i < numMessages; i++) {
            String recipient = JOptionPane.showInputDialog("Enter recipient cell number (+XXXXXXXXXXX):");
            String text = JOptionPane.showInputDialog("Enter message (max 250 characters):");

            if (text.length() > 250) {
                JOptionPane.showMessageDialog(null, "Message exceeds 250 characters. Please shorten your message.");
                i--;
                continue;
            }

            Message msg = new Message(recipient, text);
            int option = JOptionPane.showConfirmDialog(null, msg.printMessage() + "\n\nDo you want to send this message?", "Send?", JOptionPane.YES_NO_CANCEL_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                sentMessages.add(msg);
                messageIDs.add(msg.getMessageID());
                messageHashes.add(msg.getMessageHash());
                JOptionPane.showMessageDialog(null, "Message successfully sent.");
            } else if (option == JOptionPane.CANCEL_OPTION) {
                storedMessages.add(msg);
                saveStoredMessages();
                JOptionPane.showMessageDialog(null, "Message stored for later.");
            } else {
                disregardedMessages.add(msg);
                JOptionPane.showMessageDialog(null, "Message disregarded.");
            }
        }
    }

    // This method saves stored messages to a JSON file
    public void saveStoredMessages() {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            new Gson().toJson(storedMessages, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save stored messages.");
        }
    }

    // This method loads messages from the JSON file
    public void loadStoredMessages() {
        try (Reader reader = new FileReader(JSON_FILE)) {
            storedMessages = new Gson().fromJson(reader, new TypeToken<ArrayList<Message>>() {}.getType());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No stored messages to load.");
        }
    }

    // This method finds and shows the longest message that was sent
    public void displayLongestMessage() {
        Message longest = sentMessages.stream().max(Comparator.comparingInt(m -> m.getMessageText().length())).orElse(null);
        if (longest != null) JOptionPane.showMessageDialog(null, "Longest message:\n" + longest.printMessage());
        else JOptionPane.showMessageDialog(null, "No messages sent.");
    }

    // This lets the user search for a message by its ID
    public void searchByMessageID(String id) {
        for (Message m : sentMessages) {
            if (m.getMessageID().equals(id)) {
                JOptionPane.showMessageDialog(null, "Found:\nRecipient: " + m.getRecipient() + "\nMessage: " + m.getMessageText());
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Message ID not found.");
    }

    // This searches for all messages sent to a specific recipient
    public void searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            if (m.getRecipient().equals(recipient)) {
                sb.append(m.printMessage()).append("\n\n");
            }
        }
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No messages to that recipient.");
    }

    // This deletes a message from the list using its hash code
    public void deleteByHash(String hash) {
        Iterator<Message> it = sentMessages.iterator();
        while (it.hasNext()) {
            Message m = it.next();
            if (m.getMessageHash().equals(hash)) {
                it.remove();
                JOptionPane.showMessageDialog(null, "Message \"" + m.getMessageText() + "\" successfully deleted.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "No message found with that hash.");
    }

    // This displays a report of all the messages that were sent
    public void displayReport() {
        StringBuilder sb = new StringBuilder("--- Sent Message Report ---\n");
        for (Message m : sentMessages) {
            sb.append(m.printMessage()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    // This is where the app starts running
    public static void main(String[] args) {
        PoePartOne app = new PoePartOne();
        app.loadStoredMessages();

        JOptionPane.showMessageDialog(null, "Registration:");
        while (true) {
            String username = JOptionPane.showInputDialog("Enter username:");
            String password = JOptionPane.showInputDialog("Enter password:");
            String phone = JOptionPane.showInputDialog("Enter SA phone number (+27...):");
            String result = app.registerUser(username, password, phone);

            if (result.contains("successfully")) {
                JOptionPane.showMessageDialog(null, result);
                break;
            } else {
                JOptionPane.showMessageDialog(null, result);
            }
        }

        String loginUsername = JOptionPane.showInputDialog("Enter username:");
        String loginPassword = JOptionPane.showInputDialog("Enter password:");

        Login login = new Login(app.username, app.password);

        if (login.loginUser(loginUsername, loginPassword)) {
            while (true) {
                String menu = JOptionPane.showInputDialog("Welcome to QuickChat\n\nSelect an option:\n1) Send Message\n2) View Sent Messages\n3) View Longest Message\n4) Search by Message ID\n5) Search by Recipient\n6) Delete by Message Hash\n7) Display Report\n8) Quit");
                switch (menu) {
                    case "1": app.sendMessageFlow(); break;
                    case "2": app.displayReport(); break;
                    case "3": app.displayLongestMessage(); break;
                    case "4": app.searchByMessageID(JOptionPane.showInputDialog("Enter Message ID:")); break;
                    case "5": app.searchByRecipient(JOptionPane.showInputDialog("Enter Recipient Number:")); break;
                    case "6": app.deleteByHash(JOptionPane.showInputDialog("Enter Message Hash:")); break;
                    case "7": app.displayReport(); break;
                    case "8": System.exit(0); break;
                    default: JOptionPane.showMessageDialog(null, "Invalid choice.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Login failed.");
        }
    }
}
