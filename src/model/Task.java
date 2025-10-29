package model;

public class Task {
//    counter for unique task IDs
    private static int counter = 1;
    private int id;
    private String description;
    private String assignedTo;
    private String status;

    public Task(String description, String assignedTo) {
        this.id = counter++;
        this.description = description;
        this.assignedTo = assignedTo;
        this.status = "Pending";
    }

    public int getId() { return id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Task#" + id + ": " + description + " -> " + assignedTo +
                " (" + status + ")";
    }
}
