package service;

import model.*;

public class AuthService {
    private DataStore store;

    public AuthService(DataStore store) { this.store = store; }

    public User login(String username, String password) {
        for (User u : store.users)
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return u;
        return null;
    }
}
