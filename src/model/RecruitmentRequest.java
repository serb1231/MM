package model;

public class RecruitmentRequest {
    private static int counter = 1;
    private int id;
    private String department;
    private String reason;
    private String status;
    private int numberOfPositions;

    public RecruitmentRequest(String department, String reason, int numberOfPositions) {
        this.id = counter++;
        this.department = department;
        this.reason = reason;
        this.status = "Pending HR";
        this.numberOfPositions = numberOfPositions;
    }

    public int getId() { return id; }
    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Recruitment#" + id + " (" + department + "): " + reason +
                " [" + status + "]" + " - Positions: " + numberOfPositions;
    }

    public String getDepartment() {
        return department;
    }

    public int getNumberOfPositions() {
        return numberOfPositions;
    }
}
