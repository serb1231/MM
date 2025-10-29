package service;

import model.*;
import java.util.*;

public class SubTeamService {
    private final DataStore store;

    public SubTeamService(DataStore store) {
        this.store = store;
    }

    public void listTeams() {
        System.out.println("\n--- Available Teams ---");
        store.subteams.values().forEach(System.out::println);
    }

    public void listMembers(String teamName) {
        SubTeamRequest team = store.subteams.get(teamName);
        if (team == null) {
            System.out.println("⚠️ No such team found.");
            return;
        }

        System.out.println("\nMembers of " + teamName + ":");
        if (team.getMembers().isEmpty()) System.out.println("No members yet.");
        else team.getMembers().forEach(m -> System.out.println("• " + m));
    }

//    list all members of all teams
    public void listAllMembers() {
        System.out.println("\n--- All Team Members ---");
        store.subteams.values().forEach(team -> {
            System.out.println("\nMembers of " + team.getTeamName() + ":");
            if (team.getMembers().isEmpty()) {
                System.out.println("No members yet.");
            } else {
                team.getMembers().forEach(m -> System.out.println("• " + m));
            }
        });
    }

    public void addMember(String teamName, String newMember) {
        SubTeamRequest team = store.subteams.get(teamName);
        if (team == null) {
            System.out.println("⚠️ No such team.");
            return;
        }
        team.addMember(newMember);
        System.out.println("✅ Added " + newMember + " to " + teamName);
    }

    public void assignTask(String teamName, Task task) {
        SubTeamRequest team = store.subteams.get(teamName);
        if (team == null) {
            System.out.println("⚠️ Team not found.");
            return;
        }
        team.addTask(task);
        System.out.println("✅ Task assigned to " + teamName);
    }

    public void listTasks(String teamName) {
        SubTeamRequest team = store.subteams.get(teamName);
        if (team == null) {
            System.out.println("⚠️ No such team.");
            return;
        }
        System.out.println("\n--- Tasks for " + teamName + " ---");
        if (team.getTasks().isEmpty()) System.out.println("No tasks yet.");
        else team.getTasks().forEach(System.out::println);
    }

    public void respondToTask(String teamName, int taskId, boolean accepted, String reason) {
        SubTeamRequest team = store.subteams.get(teamName);
        if (team == null) {
            System.out.println("⚠️ Team not found.");
            return;
        }

        Optional<Task> t = team.getTasks().stream()
                .filter(task -> task.getId() == taskId)
                .findFirst();

        if (t.isEmpty()) {
            System.out.println("⚠️ Task not found.");
            return;
        }

        String status = accepted ? "Accepted ✅" : "Rejected ❌ (" + reason + ")";
        t.get().setStatus(status);
        System.out.println("📝 Task#" + t.get().getId() + " marked as " + status);
    }
}
