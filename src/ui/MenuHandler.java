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
    private final SubTeamService subteams;

    public MenuHandler(AuthService auth, EventService events, TaskService tasks, HRService hr, FinanceService finance, SubTeamService subteams) {
        this.auth = auth;
        this.events = events;
        this.tasks = tasks;
        this.hr = hr;
        this.finance = finance;
        this.subteams = subteams;
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

//    we should return if it is active
    private boolean LogicForSMorPM() {
        System.out.println("1. Assign Task  2. Request Recruitment  3. Request Finance  4. View All (Event)  5. Logout");
        int pm = Integer.parseInt(sc.nextLine());
        if (pm >= 1 && pm <= 4) {
            events.listEvents();
            System.out.print("Enter Event ID: ");
            int eId = Integer.parseInt(sc.nextLine());

            if (pm == 1) {
                // Show available teams for this event
                System.out.println("\n📋 Available Teams for Event#" + eId + ":");
//                events.getAllEvents().stream()
//                        .filter(ev -> ev.getId() == eId)
//                        .findFirst()
//                        .ifPresent(EventRequest::printDepartments);
                subteams.listTeams();


                System.out.println();

                System.out.print("Task Description: ");
                String td = sc.nextLine();

                System.out.print("Assign to existing team? (yes/no): ");
                String assignChoice = sc.nextLine().trim().toLowerCase();

                String assignedTeam = "Unassigned";
                if (assignChoice.equals("yes")) {
                    System.out.println("\n📋 Available Teams for Event#" + eId + ":");
//                    events.getAllEvents().stream()
//                            .filter(ev -> ev.getId() == eId)
//                            .findFirst()
//                            .ifPresent(EventRequest::printDepartments);
                    subteams.listTeams();
                    System.out.print("Enter team name (exactly as shown): ");
                    assignedTeam = sc.nextLine().trim();
                }

                tasks.createTask(eId, td, assignedTeam);
            }
            else if (pm == 2) { // Request Recruitment
                System.out.println("\n📋 Available Teams for Event#" + eId + ":");
//                events.getAllEvents().stream()
//                        .filter(ev -> ev.getId() == eId)
//                        .findFirst()
//                        .ifPresent(EventRequest::printDepartments);
                subteams.listTeams();
                System.out.print("Enter team Name (exactly as shown: ");
                String dept = sc.nextLine().trim();

                System.out.print("Number of people to recruit: ");
                int count = Integer.parseInt(sc.nextLine());

                System.out.print("Reason for Recruitment: ");
                String reason = sc.nextLine();

                hr.requestRecruitment(eId, dept, reason, count);

//                System.out.println("👥 Current members of '" + dept + "': " +
//                        (team.getMembers().isEmpty() ? "None" : String.join(", ", team.getMembers())));
                subteams.listMembers(dept);

                // Step 4: Feedback
                System.out.println("✅ Recruitment request for SubTeam '" + dept + "' with " + count + " positions has been created.");
            }
            else if (pm == 3) {
                System.out.print("Requested Amount ($): ");
                double amount = Double.parseDouble(sc.nextLine());
                System.out.print("Finance Request Details: ");
                String d = sc.nextLine();
                String details = d + " [Requested $" + amount + "]";
                finance.requestFinance(eId, "ProductionManager/ServiceManager", details);
            } else if (pm == 4) {
                tasks.listTasks(eId);
                hr.listRecruitments(eId);
                finance.listFinances(eId);
            }
        } else {
            return false;
        }
        return true;
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
                    System.out.println("1. Approve/Reject Budgets  2. View Finance Requests by Event  3. Logout");
                    String fmInput = sc.nextLine().trim();

                    switch (fmInput) {
                        case "1":
                            events.listEvents();
                            System.out.print("Enter Event ID: ");
                            int eId = Integer.parseInt(sc.nextLine());
                            finance.listFinances(eId);
                            System.out.print("Enter Finance ID to update: ");
                            int fId = Integer.parseInt(sc.nextLine());
                            System.out.print("Decision (Approved/Rejected): ");
                            String st = sc.nextLine();
                            finance.processFinance(eId, fId, st);
                            break;

                        case "2":
                            events.listEvents();
                            System.out.print("Enter Event ID: ");
                            int viewId = Integer.parseInt(sc.nextLine());
                            finance.listFinances(viewId);
                            break;

                        case "3":
                            active = false;
                            break;

                        default:
                            System.out.println("⚠️ Invalid input. Please choose 1, 2, or 3.");
                            break;
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
                    active = LogicForSMorPM();
                    break;

                // -------------------- SERVICE MANAGER --------------------
                case "ServiceManager":
                    System.out.println("\n[Service Manager Menu]");
                    active = LogicForSMorPM();
                    break;

                // -------------------- HR --------------------
                case "HR":
                    System.out.println("\n[HR Menu]");
                    System.out.println("1. View Recruitment Requests (Event)  2. Process Recruitment  3. Manage SubTeams  4. Logout");
                    int hrChoice = Integer.parseInt(sc.nextLine());

                    if (hrChoice == 1 || hrChoice == 2 || hrChoice == 3) {
                        // Step 1: List all events
                        events.listEvents();
                        System.out.print("Enter Event ID: ");
                        int eId = Integer.parseInt(sc.nextLine());

                        // Step 2: Show departments and their members first
                        System.out.println();
                        System.out.println("📋 Current Department Members for this Event:");
                        events.getAllEvents().stream()
                                .filter(ev -> ev.getId() == eId)
                                .findFirst()
                                .ifPresentOrElse(
                                        event -> {subteams.listAllMembers();},
                                        () -> System.out.println("⚠️ Event not found.")
                                );

                        System.out.println();

                        // Step 3: Handle HR actions for that event
                        if (hrChoice == 1) {
                            hr.listRecruitments(eId);

                        } else if (hrChoice == 2) {
                            hr.listRecruitments(eId);
                            System.out.print("Enter Recruitment ID: ");
                            int id = Integer.parseInt(sc.nextLine());
                            System.out.print("Decision (Approved/Rejected): ");
                            String st = sc.nextLine();
                            hr.processRecruitment(eId, id, st);

                        } else if (hrChoice == 3) {
                            // --- Manage SubTeams ---
                            subteams.listTeams();
                            System.out.print("Select SubTeam Name: ");
                            String tName = sc.nextLine();

                            System.out.println("1. View Members  2. Add Member");
                            int sub = Integer.parseInt(sc.nextLine());

                            if (sub == 1) {
                                subteams.listMembers(tName);
                            } else if (sub == 2) {
                                System.out.print("Enter New Member Name: ");
                                String newMem = sc.nextLine();
                                subteams.addMember(tName, newMem);
                            }
                        }

                    } else {
                        active = false;
                    }
                    break;


                // -------------------- SUBTEAM LEADERS --------------------
                case "Catering":
                case "Logistics":
                case "Security":
                case "Decoration":
                    System.out.println("\n[SubTeam Leader Menu]");
                    System.out.println("1. View My Tasks  2. Respond to Task  3. View Members  4. Logout");
                    int stChoice = Integer.parseInt(sc.nextLine());
                    if (stChoice != 4) {
                        System.out.print("Enter Your Team Name: ");
                    }
                    String team = sc.nextLine();

                    if (stChoice == 1) {
                        subteams.listTasks(team);
                    } else if (stChoice == 2) {
                        subteams.listTasks(team);
                        System.out.print("Enter Task ID: ");
                        int tId = Integer.parseInt(sc.nextLine());
                        System.out.print("Accept or Reject? (A/R): ");
                        String ans = sc.nextLine().trim();
                        boolean accepted = ans.equalsIgnoreCase("A");
                        String reason = "";
                        if (!accepted) {
                            System.out.print("Reason for rejection: ");
                            reason = sc.nextLine();
                        }
                        subteams.respondToTask(team, tId, accepted, reason);
                    } else if (stChoice == 3) {
                        subteams.listMembers(team);
                    } else {
                        active = false;
                    }
                    break;


                // -------------------- VICE PRESIDENT --------------------
                case "VicePresident":
                    System.out.println("\n[Vice President Menu]");
                    System.out.println("1. View All Reports by Event  2. Logout");
                    int v = Integer.parseInt(sc.nextLine());
                    if (v == 1) {
                        events.listEvents();
                        System.out.print("Enter Event ID: ");
                        int eId = Integer.parseInt(sc.nextLine());
                        tasks.listTasks(eId);
                        hr.listRecruitments(eId);
                        finance.listFinances(eId);
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
