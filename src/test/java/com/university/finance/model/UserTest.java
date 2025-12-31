package com.university.finance.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaires pour la classe User.
 */
public class UserTest {
    
    @Test
    public void testUserCreation() {
        User user = new User("testuser", "password123");
        
        assertEquals("testuser", user.getUsername());
        assertNotNull(user.getCreatedAt());
        assertNull(user.getLastLogin());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUserCreationWithEmptyUsername() {
        new User("", "password123");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUserCreationWithShortPassword() {
        new User("testuser", "123");
    }
    
    @Test
    public void testVerifyPassword() {
        User user = new User("testuser", "password123");
        
        assertTrue(user.verifyPassword("password123"));
        assertFalse(user.verifyPassword("wrongpassword"));
    }
    
    @Test
    public void testUpdatePassword() {
        User user = new User("testuser", "password123");
        user.updatePassword("newpassword456");
        
        assertFalse(user.verifyPassword("password123"));
        assertTrue(user.verifyPassword("newpassword456"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePasswordWithShortPassword() {
        User user = new User("testuser", "password123");
        user.updatePassword("123");
    }
    
    @Test
    public void testUpdateLastLogin() {
        User user = new User("testuser", "password123");
        assertNull(user.getLastLogin());
        
        user.updateLastLogin();
        assertNotNull(user.getLastLogin());
    }
}
