package model;

import java.util.ArrayList;
import java.util.List;

public class EventRequest {
    private static int counter = 1;
    private int id;
    private String clientName;
    private String eventType;
    private String status;
    private String notes;

    // New fields
    private double budget;
    private List<Task> tasks;
    private List<FinancialRequest> finances;
    private List<RecruitmentRequest> recruitments;

    public EventRequest(String clientName, String eventType) {
        this.id = counter++;
        this.clientName = clientName;
        this.eventType = eventType;
        this.status = "New";
        this.notes = "";
        this.budget = 0.0;

        // initialize per-event lists
        this.tasks = new ArrayList<>();
        this.finances = new ArrayList<>();
        this.recruitments = new ArrayList<>();
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

    public List<Task> getTasks() { return tasks; }
    public List<FinancialRequest> getFinances() { return finances; }
    public List<RecruitmentRequest> getRecruitments() { return recruitments; }

    // Convenience adders
    public void addTask(Task t) { tasks.add(t); }
    public void addFinance(FinancialRequest f) { finances.add(f); }
    public void addRecruitment(RecruitmentRequest r) { recruitments.add(r); }

    @Override
    public String toString() {
        return "Event#" + id + " [" + eventType + "] for " + clientName +
                " - Status: " + status + " - Budget: $" + budget +
                " - Notes: " + notes;
    }
}
