package service;

import model.*;
import java.util.*;

public class DataStore {
    public List<User> users = new ArrayList<>();
    public List<EventRequest> events = new ArrayList<>();

    // ✅ Now we store SubTeam objects, not just strings
    public Map<String, SubTeamRequest> subteams = new HashMap<>();

    public DataStore() {
        users.add(new User("alice", "1234", "CustomerService"));
        users.add(new User("janet", "1234", "SeniorCS"));
        users.add(new User("bob", "1234", "FinancialManager"));
        users.add(new User("mike", "1234", "AdminManager"));
        users.add(new User("jack", "1234", "ProductionManager"));
        users.add(new User("emma", "1234", "ServiceManager"));
        users.add(new User("simon", "1234", "HR"));
        users.add(new User("vp", "1234", "VicePresident"));
//        users.add(new User("peter", "1234", "ProductionManager"));

        users.add(new User("catlead", "1234", "Catering"));
        users.add(new User("loglead", "1234", "Logistics"));
        users.add(new User("seclead", "1234", "Security"));
        users.add(new User("declead", "1234", "Decoration"));

        // ✅ Predefined SubTeams
        subteams.put("Catering", new SubTeamRequest("Catering"));
        subteams.put("Logistics", new SubTeamRequest("Logistics"));
        subteams.put("Security", new SubTeamRequest("Security"));
        subteams.put("Decoration", new SubTeamRequest("Decoration"));
    }
}
