package model;

public class EventRequest {
    private static int counter = 1;
    private int id;
    private String clientName;
    private String eventType;
    private String status;
    private String notes;

    public EventRequest(String clientName, String eventType) {
        this.id = counter++;
        this.clientName = clientName;
        this.eventType = eventType;
        this.status = "New";
        this.notes = "";
    }

    public int getId() { return id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Event#" + id + " [" + eventType + "] for " + clientName +
                " - Status: " + status + " - Notes: " + notes;
    }
}
