package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private DataStore store;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        store = new DataStore();
        store.users.add(new User("john", "1234", "CustomerService"));
        store.users.add(new User("janet", "abcd", "SeniorCS"));
        authService = new AuthService(store);
    }

    @Test
    void loginValidCredential() {
        User result = authService.login("john", "1234");
        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("CustomerService", result.getRole());
    }

    @Test
    void loginInvalidPassword() {
        User result = authService.login("john", "not");
        assertNull(result);
    }

    @Test
    void loginInvalidUsername() {
        User result = authService.login("serb", "1234");
        assertNull(result);
    }

    @Test
    void loginInvalidUppercaseSensitive() {
        User result = authService.login("John", "1234");
        assertNull(result);
    }
}
