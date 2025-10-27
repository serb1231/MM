package service;

import model.*;
import java.util.*;

public class DataStore {
    public List<User> users = new ArrayList<>();
    public List<EventRequest> events = new ArrayList<>();
    public List<Task> tasks = new ArrayList<>();
    public List<RecruitmentRequest> recruits = new ArrayList<>();
    public List<FinancialRequest> finances = new ArrayList<>();

    public DataStore() {
        users.add(new User("alice", "1234", "CustomerService"));
        users.add(new User("janet", "1234", "SeniorCS"));
        users.add(new User("bob", "1234", "FinancialManager"));
        users.add(new User("mike", "1234", "AdminManager"));
        users.add(new User("jack", "1234", "ProductionManager"));
        users.add(new User("emma", "1234", "ServiceManager"));
        users.add(new User("simon", "1234", "HR"));
        users.add(new User("vp", "1234", "VicePresident"));
    }
}
