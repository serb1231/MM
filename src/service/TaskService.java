package service;

import model.*;
import java.util.Optional;

public class TaskService {
    private final DataStore store;

    public TaskService(DataStore store) {
        this.store = store;
    }

    /**
     * Create a brand new task and assign it to a team for a specific event.
     */
    public void createTask(int eventId, String description, String assignedTo) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .findFirst();

        if (e.isPresent()) {
            Task t = new Task(description, assignedTo);
            e.get().addTask(t);
            System.out.println("✅ New task created and assigned to " + assignedTo + " for Event#" + eventId);
        } else {
            System.out.println("⚠️ Event not found with ID " + eventId);
        }
    }

    /**
     * Reassign an existing task (to a new team or member).
     */
    public void reassignTask(int eventId, int taskId, String newAssignee) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .findFirst();

        if (e.isEmpty()) {
            System.out.println("⚠️ Event not found with ID " + eventId);
            return;
        }

        EventRequest event = e.get();
        Optional<Task> t = event.getTasks().stream()
                .filter(task -> task.getId() == taskId)
                .findFirst();

        if (t.isPresent()) {
            Task task = t.get();
            task.setStatus("Reassigned to " + newAssignee);
            System.out.println("🔄 Task#" + taskId + " reassigned to " + newAssignee + " for Event#" + eventId);
        } else {
            System.out.println("⚠️ Task not found with ID " + taskId + " in Event#" + eventId);
        }
    }

    /**
     * Update the status of a task (e.g., Pending → In Progress → Completed).
     */
    public void updateTaskStatus(int eventId, int taskId, String status) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .findFirst();

        if (e.isEmpty()) {
            System.out.println("⚠️ Event not found with ID " + eventId);
            return;
        }

        EventRequest event = e.get();
        Optional<Task> t = event.getTasks().stream()
                .filter(task -> task.getId() == taskId)
                .findFirst();

        if (t.isPresent()) {
            t.get().setStatus(status);
            System.out.println("✅ Task#" + taskId + " status updated to: " + status);
        } else {
            System.out.println("⚠️ Task not found with ID " + taskId + " in Event#" + eventId);
        }
    }

    /**
     * List all tasks for a specific event.
     */
    public void listTasks(int eventId) {
        store.events.stream()
                .filter(e -> e.getId() == eventId)
                .findFirst()
                .ifPresentOrElse(
                        e -> {
                            if (e.getTasks().isEmpty())
                                System.out.println("No tasks for this event.");
                            else {
                                System.out.println("--- Tasks for Event#" + e.getId() + " ---");
                                e.getTasks().forEach(System.out::println);
                            }
                        },
                        () -> System.out.println("⚠️ Event not found.")
                );
    }
}
