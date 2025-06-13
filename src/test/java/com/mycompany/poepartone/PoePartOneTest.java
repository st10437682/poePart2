package com.mycompany.poepartone;

// Import necessary libraries for testing with JUnit 5
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

// This is a test class for the PoePartOne application
public class PoePartOneTest {
    private PoePartOne app;

    // This method runs before each test to set up a fresh instance of the app
    @BeforeEach
    void setUp() {
        app = new PoePartOne(); // Create a new instance of the main application class
    }

    // Test to check that the method can correctly find the longest message
    @Test
    void testLongestMessage() {
        // Add two messages: one short, one long
        app.getSentMessages().add(new Message("+27831234567", "Short message"));
        app.getSentMessages().add(new Message("+27831234567", "This is a much longer message than the first one."));

        // Find the longest message by comparing message text lengths
        Message longest = app.getSentMessages().stream().max((m1, m2) ->
                Integer.compare(m1.getMessageText().length(), m2.getMessageText().length())).orElse(null);

        // Ensure a message was found and check that it's the expected one
        assertNotNull(longest);
        assertEquals("This is a much longer message than the first one.", longest.getMessageText());
    }

    // Test to verify you can search for a message by its unique ID
    @Test
    void testSearchByMessageID() {
        // Create a message and add it to the app
        Message msg = new Message("+27830000000", "Testing message ID");
        app.getSentMessages().add(msg);

        // Store the message ID for searching
        String targetID = msg.getMessageID();

        // Search for a message with that ID in the sent messages list
        boolean found = app.getSentMessages().stream().anyMatch(m -> m.getMessageID().equals(targetID));

        // Confirm the message was found
        assertTrue(found);
    }

    // Test to count how many messages were sent to a specific recipient
    @Test
    void testSearchByRecipient() {
        // Add two messages to the same recipient and one to a different one
        app.getSentMessages().add(new Message("+27831111111", "Hello 1"));
        app.getSentMessages().add(new Message("+27831111111", "Hello 2"));
        app.getSentMessages().add(new Message("+27832222222", "Different recipient"));

        // Count how many messages went to +27831111111
        long count = app.getSentMessages().stream()
            .filter(m -> m.getRecipient().equals("+27831111111"))
            .count();

        // There should be exactly 2
        assertEquals(2, count);
    }

    // Test to check if a message can be deleted using its hash value
    @Test
    void testDeleteByHash() {
        // Create a message and add it to the sent messages
        Message msg = new Message("+27839999999", "To be deleted");
        app.getSentMessages().add(msg);

        // Get its hash and use it to delete the message
        String hash = msg.getMessageHash();
        app.deleteByHash(hash);

        // Check that the message no longer exists in the list
        boolean stillExists = app.getSentMessages().stream().anyMatch(m -> m.getMessageHash().equals(hash));
        assertFalse(stillExists); // The message should be deleted
    }

    // Test to ensure the message hash is formatted correctly
    @Test
    void testMessageHashFormat() {
        // Create a message and retrieve its hash
        Message msg = new Message("+27837777777", "Hey you!");
        String hash = msg.getMessageHash();

        // Confirm the hash matches the expected pattern (e.g., 00:1:HEYYOU!)
        assertTrue(hash.matches("^\\d{2}:\\d+:\\w+\\w+$"));
    }
}
