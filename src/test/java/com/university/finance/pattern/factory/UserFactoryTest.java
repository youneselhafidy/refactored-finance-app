package com.university.finance.pattern.factory;

import com.university.finance.model.User;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaires pour UserFactory.
 */
public class UserFactoryTest {
    
    @Test
    public void testCreateUser() {
        User user = UserFactory.createUser("testuser", "password123");
        
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertTrue(user.verifyPassword("password123"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithShortUsername() {
        UserFactory.createUser("ab", "password123");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithInvalidUsername() {
        UserFactory.createUser("test@user", "password123");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithShortPassword() {
        UserFactory.createUser("testuser", "123");
    }
    
    @Test
    public void testCreateTestUser() {
        User user = UserFactory.createTestUser("test", "pass");
        
        assertNotNull(user);
        assertEquals("test", user.getUsername());
    }
}
