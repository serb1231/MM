package ui;

import model.*;
import service.*;
import java.util.Scanner;

public class MenuHandler {
    private final AuthService auth;
    private final EventService events;
    private final TaskService tasks;
    private final HRService hr;
    private final FinanceService finance;
    private final Scanner sc = new Scanner(System.in);

    public MenuHandler(AuthService auth, EventService events, TaskService tasks, HRService hr, FinanceService finance) {
        this.auth = auth;
        this.events = events;
        this.tasks = tasks;
        this.hr = hr;
        this.finance = finance;
    }

    public void start() {
        while (true) {
            System.out.print("\nUsername: ");
            String u = sc.nextLine();
            System.out.print("Password: ");
            String p = sc.nextLine();

            User user = auth.login(u, p);
            if (user == null) {
                System.out.println("❌ Invalid credentials!");
                continue;
            }

            System.out.println("\nWelcome, " + user.getUsername() + " (" + user.getRole() + ")");
            handleRole(user);
        }
    }

    private void handleRole(User u) {
        boolean active = true;
        while (active) {
            switch (u.getRole()) {

                // -------------------- CUSTOMER SERVICE --------------------
                case "CustomerService":
                    System.out.println("\n[Customer Service Menu]");
                    System.out.println("1. Create Event  2. View Events  3. Logout");
                    int cs = Integer.parseInt(sc.nextLine());
                    if (cs == 1) {
                        System.out.print("Client: ");
                        String c = sc.nextLine();
                        System.out.print("Event Type: ");
                        String t = sc.nextLine();
                        events.createEvent(c, t);
                    } else if (cs == 2) {
                        events.listEvents();
                    } else {
                        active = false;
                    }
                    break;

                // -------------------- SENIOR CUSTOMER SERVICE --------------------
                case "SeniorCS":
                    System.out.println("\n[Senior Customer Service Menu]");
                    System.out.println("1. Review/Update Events  2. View Events  3. Logout");
                    int scs = Integer.parseInt(sc.nextLine());
                    if (scs == 1) {
                        events.listEvents();
                        System.out.print("Enter Event ID to update: ");
                        int id = Integer.parseInt(sc.nextLine());
                        System.out.print("New Status: ");
                        String st = sc.nextLine();
                        System.out.print("Notes: ");
                        String nt = sc.nextLine();
                        events.updateStatus(id, st, nt);
                    } else if (scs == 2) {
                        events.listEvents();
                    } else {
                        active = false;
                    }
                    break;

                // -------------------- FINANCIAL MANAGER --------------------
                case "FinancialManager":
                    System.out.println("\n[Financial Manager Menu]");
                    System.out.println("1. Approve/Reject Budgets  2. View Finance Requests  3. Logout");
                    int fm = Integer.parseInt(sc.nextLine());
                    if (fm == 1) {
//                        events.listEvents();
                        finance.listFinances();
                        System.out.print("Enter Finance ID to update: ");
                        int id = Integer.parseInt(sc.nextLine());
                        System.out.print("New Status: ");
                        String st = sc.nextLine();
                        finance.processFinance(id, st);
//                        events.updateStatus(id, st, "Reviewed by Finance Manager");
                    } else if (fm == 2) {
                        finance.listFinances();
                    } else {
                        active = false;
                    }
                    break;

                // -------------------- ADMIN MANAGER --------------------
                case "AdminManager":
                    System.out.println("\n[Admin Manager Menu]");
                    System.out.println("1. Final Approval of Event  2. Logout");
                    int am = Integer.parseInt(sc.nextLine());
                    if (am == 1) {
                        events.listEvents();
                        System.out.print("Enter Event ID to approve: ");
                        int id = Integer.parseInt(sc.nextLine());
                        events.updateStatus(id, "Approved", "Final approval by Admin Manager");
                    } else {
                        active = false;
                    }
                    break;

                // -------------------- PRODUCTION MANAGER --------------------
                case "ProductionManager":
                    System.out.println("\n[Production Manager Menu]");
                    System.out.println("1. Assign Task  2. Request Recruitment  3. Request Finance  4. View All  5. Logout");
                    int pm = Integer.parseInt(sc.nextLine());
                    if (pm == 1) {
                        System.out.print("Task Description: ");
                        String td = sc.nextLine();
                        System.out.print("Team Name: ");
                        String t = sc.nextLine();
                        tasks.assignTask(td, t);
                    } else if (pm == 2) {
                        System.out.print("Reason for Recruitment: ");
                        String r = sc.nextLine();
                        hr.requestRecruitment("Production", r);
                    } else if (pm == 3) {
                        System.out.print("Finance Request Details: ");
                        String d = sc.nextLine();
                        finance.requestFinance("ProductionManager", d);
                    } else if (pm == 4) {
                        tasks.listTasks();
                        hr.listRecruitments();
                        finance.listFinances();
                    } else {
                        active = false;
                    }
                    break;

                // -------------------- SERVICE MANAGER --------------------
                case "ServiceManager":
                    System.out.println("\n[Service Manager Menu]");
                    System.out.println("1. Assign Task  2. Request Recruitment  3. Request Finance  4. View All  5. Logout");
                    int sm = Integer.parseInt(sc.nextLine());
                    if (sm == 1) {
                        System.out.print("Task Description: ");
                        String td = sc.nextLine();
                        System.out.print("Team Name: ");
                        String t = sc.nextLine();
                        tasks.assignTask(td, t);
                    } else if (sm == 2) {
                        System.out.print("Reason for Recruitment: ");
                        String r = sc.nextLine();
                        hr.requestRecruitment("Service", r);
                    } else if (sm == 3) {
                        System.out.print("Finance Request Details: ");
                        String d = sc.nextLine();
                        finance.requestFinance("ServiceManager", d);
                    } else if (sm == 4) {
                        tasks.listTasks();
                        hr.listRecruitments();
                        finance.listFinances();
                    } else {
                        active = false;
                    }
                    break;

                // -------------------- HR --------------------
                case "HR":
                    System.out.println("\n[HR Menu]");
                    System.out.println("1. View Recruitment Requests  2. Process Recruitment  3. Logout");
                    int hrChoice = Integer.parseInt(sc.nextLine());
                    if (hrChoice == 1) {
                        hr.listRecruitments();
                    } else if (hrChoice == 2) {
                        hr.listRecruitments();
                        System.out.print("Enter Recruitment ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        System.out.print("New Status: ");
                        String st = sc.nextLine();
                        hr.processRecruitment(id, st);
                    } else {
                        active = false;
                    }
                    break;

                // -------------------- VICE PRESIDENT --------------------
                case "VicePresident":
                    System.out.println("\n[Vice President Menu]");
                    System.out.println("1. View All Reports  2. Logout");
                    int v = Integer.parseInt(sc.nextLine());
                    if (v == 1) {
                        events.listEvents();
                        tasks.listTasks();
                        hr.listRecruitments();
                        finance.listFinances();
                    } else {
                        active = false;
                    }
                    break;

                // -------------------- DEFAULT --------------------
                default:
                    System.out.println("⚠️ Unknown role: " + u.getRole());
                    active = false;
                    break;
            }
        }
    }
}
