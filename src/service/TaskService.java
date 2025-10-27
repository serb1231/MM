package service;

import model.Task;
import java.util.List;

public class TaskService {
    private final DataStore store;

    public TaskService(DataStore store) {
        this.store = store;
    }

    // Called by Production or Service Managers to assign new tasks to subteams
    public void assignTask(String description, String assignedTo) {
        store.tasks.add(new Task(description, assignedTo));
        System.out.println("✅ Task assigned to " + assignedTo);
    }

    // Called by any manager or team member to view current tasks
    public void listTasks() {
        if (store.tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            System.out.println("--- Task List ---");
            for (Task t : store.tasks) {
                System.out.println(t);
            }
        }
    }

    // Called to mark a task as completed or update its status
    public void updateTaskStatus(int id, String status) {
        for (Task t : store.tasks) {
            if (t.getId() == id) {
                t.setStatus(status);
                System.out.println("✅ Task #" + id + " updated to: " + status);
                return;
            }
        }
        System.out.println("⚠️ No task found with ID " + id);
    }
}
