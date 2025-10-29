package ui;

import model.*;
import service.*;

import java.util.List;
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
                System.out.println("Invalid credentials!");
                continue;
            }

            System.out.println("\nWelcome, " + user.getUsername() + " (" + user.getRole() + ")");
            handleRole(user);
        }
    }

//    for parsing the input and getting the integer, as it is
//    used in multiple instances
    private int getIntInput(String line) {
        while (true) {
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                line = sc.nextLine();
            }
        }
    }

//    we should return if it is active
    private boolean LogicForSMorPM() {
        System.out.println("1. Assign Task  2. Request Recruitment  3. Request Finance  4. View All (Event)  5. View Events  6. Logout");
        int pm = getIntInput(sc.nextLine());
        if (pm == 5) {
            System.out.println("\nAll Events:");
            events.listEvents();
            return true;
        }
        if (pm >= 1 && pm <= 4) {
            List<Integer> EventsId = events.listEvents();
            if (EventsId.isEmpty()) {
                System.out.println("No events available.");
                return true;
            }
            System.out.print("Enter Event ID: ");
            int eId = getIntInput(sc.nextLine());
            if (!EventsId.contains(eId)) {
                System.out.println("Invalid Event ID.");
                return true;
            }

            if (pm == 1) {
                // Show available teams for this event
                System.out.println("\nAvailable Teams for Event#" + eId + ":");
                subteams.listTeams();


                System.out.println();

                System.out.print("Task Description: ");
                String td = sc.nextLine();

                String assignedTeam = "Unassigned";
                System.out.println("\nAvailable Teams for Event#" + eId + ":");
                subteams.listTeams();
                System.out.print("Enter team name (exactly as shown): ");
                assignedTeam = sc.nextLine().trim();

                tasks.createTask(eId, td, assignedTeam);
            }
            else if (pm == 2) {
                System.out.println("\nAvailable Teams for Event#" + eId + ":");
                subteams.listTeams();
                System.out.print("Enter team Name (exactly as shown: ");
                String dept = sc.nextLine().trim();

                System.out.print("Number of people to recruit: ");
                int count = getIntInput(sc.nextLine());

                System.out.print("Reason for Recruitment: ");
                String reason = sc.nextLine();

                hr.requestRecruitment(eId, dept, reason, count);

                subteams.listMembers(dept);

                System.out.println("Recruitment request for SubTeam '" + dept + "' with " + count + " positions has been created.");
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

    private boolean logicForSubteam(String team) {
        System.out.println("\n[SubTeam Leader Menu]");
        System.out.println("1. View My Tasks  2. Respond to Task  3. View Members  4. Logout");
        int stChoice = getIntInput(sc.nextLine());

        if (stChoice == 1) {
            subteams.listTasks(team);
        } else if (stChoice == 2) {
            subteams.listTasks(team);
            System.out.print("Enter Task ID: ");
            int tId = getIntInput(sc.nextLine());
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
                    int cs = getIntInput(sc.nextLine());
                    if (cs == 1) {
                        System.out.print("Client: ");
                        String c = sc.nextLine();
                        System.out.print("Event Type: ");
                        String t = sc.nextLine();
                        events.createEvent(c, t);
                    } else if (cs == 2) {
                        events.listAllEvents();
                    } else {
                        active = false;
                    }
                    break;

                // -------------------- SENIOR CUSTOMER SERVICE --------------------
                case "SeniorCS":
                    System.out.println("\n[Senior Customer Service Menu]");
                    System.out.println("1. Review Pending Events  2. View all Events  3. View Events Approved by AM  4. Notify Client / Modify Final Events  5. Logout");

                    int scs = getIntInput(sc.nextLine());

                    if (scs == 1) {
                        List<Integer> pendingEvents = events.listPendingEventsForSCS();

//                        if no pending events, break
                        if (pendingEvents.isEmpty()) {
                            break;
                        }

                        System.out.print("Enter Event ID to review: ");
                        int eId = getIntInput(sc.nextLine());

//                        if the event id is not in the pending events list
                        if (!pendingEvents.contains(eId)) {
                            System.out.println("Invalid Event ID.");
                            break;
                        }

                        EventRequest event = events.getEventById(eId);
                        if (event == null) {
                            System.out.println("Event not found.");
                            break;
                        }

                        System.out.println("Current Status: " + event.getStatus());
                        System.out.print("Decision (Approve/Reject): ");
                        String decision = sc.nextLine().trim();

                        if (decision.equalsIgnoreCase("Reject")) {
                            event.setStatus("Rejected by SCS");
                            System.out.println("Event request rejected. Client will be notified.");
                        } else if (decision.equalsIgnoreCase("Approve")) {
                            event.setStatus("Approved by SCS → Pending FM Review");
                            System.out.println("Event approved and forwarded to Financial Manager.");
                        } else {
                            System.out.println("Invalid input. Must be Approve or Reject.");
                        }

                    } else if (scs == 2) {
                        events.listAllEvents();

                    } else if (scs == 3) {
                        System.out.println("\nEvents Approved by AM (Pending Client Notification):");
                        events.listPendingEventsForFinalSCS();

                    } else if (scs == 4) {
                        System.out.println("\nEvents Approved by AM (Ready for Client Notification):");
                        List<Integer> eventsPending = events.listPendingEventsForFinalSCS();

                        if (eventsPending.isEmpty()) {
                            break;
                        }

                        System.out.print("\nEnter Event ID to process: ");
                        int eId = getIntInput(sc.nextLine());

                        if (!eventsPending.contains(eId)) {
                            System.out.println("Invalid Event ID.");
                            break;
                        }

                        EventRequest event = events.getEventById(eId);
                        if (event == null) {
                            System.out.println("Event not found.");
                            break;
                        }

                        if (!event.getStatus().equalsIgnoreCase("Approved by AM → Notify SCS/Client")) {
                            System.out.println("This event is not ready for client notification.");
                            break;
                        }

                        System.out.print("Would you like to (1) Notify Client or (2) Modify Notes? ");
                        int action = getIntInput(sc.nextLine());

                        if (action == 1) {
                            System.out.println("Client has been notified of approval for Event #" + eId + ".");
                            event.setStatus("OK");
                        } else if (action == 2) {
                            System.out.print("Enter updated notes for the client: ");
                            String notes = sc.nextLine();
                            event.setNotes("Client Notes: " + notes);
                            System.out.println("Notes updated successfully.");
                        } else {
                            System.out.println("Invalid option. Choose 1 or 2.");
                        }

                    } else if (scs == 5) {
                        active = false;
                    } else {
                        System.out.println("Invalid input. Choose between 1–5.");
                    }
                    break;

                // -------------------- FINANCIAL MANAGER --------------------
                case "FinancialManager":
                    System.out.println("\n[Financial Manager Menu]");
                    System.out.println("1. Approve/Reject Budgets  2. View Finance Requests by Event  3. View Pending Events  4. Review Pending Event  5. Logout");
                    String fmInput = sc.nextLine().trim();

                    switch (fmInput) {
                        case "1":
                            // Handle finance approvals
                            List<Integer> EventsId = events.listEvents();
                            System.out.print("Enter Event ID: ");
                            int eId = getIntInput(sc.nextLine());
                            if (!EventsId.contains(eId)) {
                                System.out.println("Invalid Event ID.");
                                break;
                            }
                            finance.listFinances(eId);
                            System.out.print("Enter Finance ID to update: ");
                            int fId = getIntInput(sc.nextLine());
                            System.out.print("Decision (Approved/Rejected): ");
                            String st = sc.nextLine();
                            finance.processFinance(eId, fId, st);
                            break;

                        case "2":
                            // View finance requests for an event
                            events.listEvents();
                            System.out.print("Enter Event ID: ");
                            int viewId = getIntInput(sc.nextLine());
                            finance.listFinances(viewId);
                            break;

                        case "3":
                            // View events pending FM review
                            System.out.println("\nEvents Pending Financial Review:");
                            events.listPendingEventsForFM(); // reuse the same method
                            break;

                        case "4":
                            // Review & decide on a pending event
                            System.out.println("\nEvents Pending Financial Review:");
                            List<Integer> pendingEvents = events.listPendingEventsForFM();

                            if (pendingEvents.isEmpty()) {
                                break;
                            }

                            System.out.print("\nEnter Event ID to review: ");
                            int pendingId = getIntInput(sc.nextLine());
                            if (!pendingEvents.contains(pendingId)) {
                                System.out.println("Invalid Event ID.");
                                break;
                            }
                            EventRequest pendingEvent = events.getEventById(pendingId);

                            if (pendingEvent == null) {
                                System.out.println("Event not found.");
                                break;
                            }

                            System.out.print("Enter Financial Feedback / Notes: ");
                            String feedback = sc.nextLine();

                            System.out.print("Decision (Approve/Reject): ");
                            String decision = sc.nextLine().trim();

                            if (decision.equalsIgnoreCase("Approve")) {
                                pendingEvent.setStatus("Approved by FM → Pending Admin Approval");
                                pendingEvent.setNotes("FM feedback: " + feedback);
                                System.out.println("Event approved and forwarded to Admin Manager.");
                            } else if (decision.equalsIgnoreCase("Reject")) {
                                pendingEvent.setStatus("Rejected by FM");
                                pendingEvent.setNotes("FM feedback: " + feedback);
                                System.out.println("Event rejected by Financial Manager.");
                            } else {
                                System.out.println("Invalid input. Must be Approve or Reject.");
                            }
                            break;

                        case "5":
                            active = false;
                            break;

                        default:
                            System.out.println("Invalid input. Please choose 1–5.");
                            break;
                    }
                    break;



                // -------------------- ADMIN MANAGER --------------------
                case "AdminManager":
                    System.out.println("\n[Admin Manager Menu]");
                    System.out.println("1. View Pending Events  2. Review Event for Final Approval  3. Logout");
                    String amInput = sc.nextLine().trim();

                    switch (amInput) {
                        case "1":
                            // Show all events waiting for AM approval
                            System.out.println("\nEvents Pending Final Approval (from FM):");
                            events.listPendingEventsForAM();
                            break;

                        case "2":
                            // Review a specific event and approve/reject
                            System.out.println("\nEvents Pending Final Approval (from FM):");
                            List<Integer> eventsPending = events.listPendingEventsForAM();

                            if (eventsPending.isEmpty()) {
                                break;
                            }

                            System.out.print("\nEnter Event ID to review: ");
                            int eId = getIntInput(sc.nextLine());

                            if (!eventsPending.contains(eId)) {
                                System.out.println("Invalid Event ID.");
                                break;
                            }

                            EventRequest event = events.getEventById(eId);
                            if (event == null) {
                                System.out.println("Event not found.");
                                break;
                            }

//                            if (!event.getStatus().equalsIgnoreCase("Approved by FM → Pending Admin Approval")) {
//                                System.out.println("This event is not pending Admin approval.");
//                                break;
//                            }

                            System.out.println("Current Status: " + event.getStatus());
                            System.out.print("Enter Review Notes / Comments: ");
                            String notes = sc.nextLine();

                            System.out.print("Decision (Approve/Reject): ");
                            String decision = sc.nextLine().trim();

                            if (decision.equalsIgnoreCase("Approve")) {
                                event.setStatus("Approved by AM → Notify SCS/Client");
                                event.setNotes("AM Decision: " + notes);
                                System.out.println("Event finally approved. SCS will notify client.");
                            } else if (decision.equalsIgnoreCase("Reject")) {
                                event.setStatus("Rejected by AM");
                                event.setNotes("AM Decision: " + notes);
                                System.out.println("Event rejected. SCS will notify client.");
                            } else {
                                System.out.println("Invalid input. Must be Approve or Reject.");
                            }
                            break;

                        case "3":
                            active = false;
                            break;

                        default:
                            System.out.println("Invalid input. Please choose 1–3.");
                            break;
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
                    int hrChoice = getIntInput(sc.nextLine());

                    if (hrChoice == 1 || hrChoice == 2 || hrChoice == 3) {
                        events.listEvents();
                        System.out.print("Enter Event ID: ");
                        int eId = getIntInput(sc.nextLine());

                        System.out.println();
                        System.out.println("Current Department Members for this Event:");
                        events.getAllEvents().stream()
                                .filter(ev -> ev.getId() == eId)
                                .findFirst()
                                .ifPresentOrElse(
                                        event -> {subteams.listAllMembers();},
                                        () -> System.out.println("Event not found.")
                                );

                        System.out.println();

                        if (hrChoice == 1) {
                            hr.listRecruitments(eId);

                        } else if (hrChoice == 2) {
                            hr.listRecruitments(eId);
                            System.out.print("Enter Recruitment ID: ");
                            int id = getIntInput(sc.nextLine());
                            System.out.print("Decision (Approved/Rejected): ");
                            String st = sc.nextLine();
                            hr.processRecruitment(eId, id, st);

                        } else if (hrChoice == 3) {
                            subteams.listTeams();
                            System.out.print("Select SubTeam Name: ");
                            String tName = sc.nextLine();

                            System.out.println("1. View Members  2. Add Member");
                            int sub = getIntInput(sc.nextLine());

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
                    active = logicForSubteam("Catering");
                    break;
                case "Logistics":
                    active = logicForSubteam("Logistics");
                    break;
                case "Security":
                    active = logicForSubteam("Security");
                    break;
                case "Decoration":
                    active = logicForSubteam("Decoration");
                    break;


                // -------------------- VICE PRESIDENT --------------------
                case "VicePresident":
                    System.out.println("\n[Vice President Menu]");
                    System.out.println("1. View All Reports by Event  2. Logout");
                    int v = getIntInput(sc.nextLine());
                    if (v == 1) {
                        events.listEvents();
                        System.out.print("Enter Event ID: ");
                        int eId = getIntInput(sc.nextLine());
                        tasks.listTasks(eId);
                        hr.listRecruitments(eId);
                        finance.listFinances(eId);
                    } else {
                        active = false;
                    }
                    break;

                // -------------------- DEFAULT --------------------
                default:
                    System.out.println("Unknown role: " + u.getRole());
                    active = false;
                    break;
            }
        }
    }
}
