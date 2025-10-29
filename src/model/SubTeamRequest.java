package model;

import java.util.*;

public class SubTeamRequest {
    private String teamName;
    private List<String> members;
    private List<Task> tasks;

    public SubTeamRequest(String teamName) {
        this.teamName = teamName;
        this.members = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public String getTeamName() { return teamName; }
    public List<String> getMembers() { return members; }
    public List<Task> getTasks() { return tasks; }

    public void addMember(String member) { members.add(member); }
    public void addTask(Task t) { tasks.add(t); }

    @Override
    public String toString() {
        return "Team: " + teamName +
                " | Members: " + (members.isEmpty() ? "None" : String.join(", ", members)) +
                " | Tasks: " + tasks.size();
    }
}
