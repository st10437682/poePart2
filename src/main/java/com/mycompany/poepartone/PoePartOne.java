// PoePartOne.java
package com.mycompany.poepartone;

import javax.swing.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class PoePartOne {
    private String username;
    private String password;
    private String cellPhoneNumber;
    private ArrayList<Message> sentMessages = new ArrayList<>();

    public boolean checkUserName(String username) {
        return username != null && !username.isEmpty() && username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        return password != null &&
                password.length() >= 8 &&
                Pattern.compile("[A-Z]").matcher(password).find() &&
                Pattern.compile("[0-9]").matcher(password).find() &&
                Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        return cellPhoneNumber != null && !cellPhoneNumber.isEmpty() &&
                Pattern.matches("^\\+27\\d{9,10}$", cellPhoneNumber);
    }

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

    public void sendMessageFlow() {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");

        int numMessages = Integer.parseInt(JOptionPane.showInputDialog("How many messages do you want to send?"));

        for (int i = 0; i < numMessages; i++) {
            String recipient = JOptionPane.showInputDialog("Enter recipient cell number (+XXXXXXXXXX):");
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
                JOptionPane.showMessageDialog(null, "Message successfully sent.");
            } else if (option == JOptionPane.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(null, "Message stored for later (not implemented)." );
            } else {
                JOptionPane.showMessageDialog(null, "Message disregarded.");
            }
        }

        JOptionPane.showMessageDialog(null, "Total messages sent: " + sentMessages.size());
    }

    public static void main(String[] args) {
        PoePartOne app = new PoePartOne();

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
            app.sendMessageFlow();
        } else {
            JOptionPane.showMessageDialog(null, "Login failed.");
        }
    }
}