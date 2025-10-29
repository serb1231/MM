package model;

public class FinancialRequest {
    private static int counter = 1;
    private int id;
    private String requester;
    private String details;
    private String status;

    public FinancialRequest(String requester, String details) {
        this.id = counter++;
        this.requester = requester;
        this.details = details;
        this.status = "Pending Finance";
    }

    public int getId() { return id; }
    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    //    getter and setter for requester and details
    public String getRequester() { return requester; }
    public void setRequester(String requester) { this.requester = requester; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }


    @Override
    public String toString() {
        return "Finance#" + id + " by " + requester + " - " + details +
                " [" + status + "]";
    }
}
