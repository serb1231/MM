package model;

import java.util.*;

public class EventRequest {
    private static int counter = 1;
    private int id;
    private String clientName;
    private String eventType;
    private String status;
    private String notes;

    private double budget;
    private double numberOfWorkers;

    private List<Task> tasks;
    private List<FinancialRequest> finances;
    private List<RecruitmentRequest> recruitments;

    // Departments and their users (per event)
//    private Map<String, List<String>> departments;

    public EventRequest(String clientName, String eventType) {
        this.id = counter++;
        this.clientName = clientName;
        this.eventType = eventType;
        this.status = "New";
        this.notes = "";
        this.budget = 0.0;
        this.numberOfWorkers = 0;

        this.tasks = new ArrayList<>();
        this.finances = new ArrayList<>();
        this.recruitments = new ArrayList<>();
//        this.departments = new HashMap<>();
    }

    // --- Getters / setters ---
    public int getId() { return id; }
    public String getClientName() { return clientName; }
    public String getEventType() { return eventType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public double getNumberOfWorkers() { return numberOfWorkers; }
    public void setNumberOfWorkers(double numberOfWorkers) { this.numberOfWorkers = numberOfWorkers; }

    public List<Task> getTasks() { return tasks; }
    public List<FinancialRequest> getFinances() { return finances; }
    public List<RecruitmentRequest> getRecruitments() { return recruitments; }

//    public Map<String, List<String>> getDepartments() { return departments; }

    // --- Convenience methods ---
    public void addTask(Task t) { tasks.add(t); }
    public void addFinance(FinancialRequest f) { finances.add(f); }
    public void addRecruitment(RecruitmentRequest r) { recruitments.add(r); }

//    public void addMemberToDepartment(String department, String memberName) {
//        departments.computeIfAbsent(department, k -> new ArrayList<>()).add(memberName);
//    }

//    public void printDepartments() {
//        if (departments.isEmpty()) {
//            System.out.println("No departments or users assigned yet.");
//        } else {
//            System.out.println("--- Departments for Event#" + id + " ---");
//            departments.forEach((dept, users) ->
//                    System.out.println("• " + dept + ": " + (users.isEmpty() ? "No members" : String.join(", ", users)))
//            );
//        }
//    }

    @Override
    public String toString() {
        return "Event#" + id + " [" + eventType + "] for " + clientName +
                " - Status: " + status +
                " - Budget: $" + budget +
                " - Workers: " + numberOfWorkers +
                " - Notes: " + notes;
    }
}
