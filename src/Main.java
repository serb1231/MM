import service.*;
import ui.MenuHandler;

public class Main {
    public static void main(String[] args) {
        // Initialize data storage (users, events, etc.)
        DataStore store = new DataStore();

        // Create service instances and inject shared data store
        AuthService auth = new AuthService(store);
        EventService events = new EventService(store);
        TaskService tasks = new TaskService(store);
        HRService hr = new HRService(store);
        FinanceService finance = new FinanceService(store);
        SubTeamService subTeamService = new SubTeamService(store);

        // Create and start the main terminal UI
        MenuHandler ui = new MenuHandler(auth, events, tasks, hr, finance, subTeamService);
        ui.start();
    }
}
