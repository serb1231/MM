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
     * It automatically links the task to both the event and the assigned team.
     */
    public void createTask(int eventId, String description, String assignedTo) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .filter(ev -> ev.getStatus() != null && ev.getStatus().contains("OK"))
                .findFirst();

        if (e.isEmpty()) {
            System.out.println("⚠️ Event not found.");
            return;
        }

        if (!store.subteams.containsKey(assignedTo)) {
            System.out.println("⚠️ Invalid team name. Use one of: " + store.subteams.keySet());
            return;
        }

        Task t = new Task(description, assignedTo);
        e.get().addTask(t);

        store.subteams.get(assignedTo).addTask(t);

        System.out.println("✅ Task created and assigned to " + assignedTo + " for Event#" + eventId);
    }

//    /**
//     * Reassign an existing task (to a new team or member).
//     */
//    public void reassignTask(int eventId, int taskId, String newAssignee) {
//        Optional<EventRequest> e = store.events.stream()
//                .filter(ev -> ev.getId() == eventId)
//                    .filter(ev -> ev.getStatus() != null && ev.getStatus().contains("OK"))
//                .findFirst();
//
//        if (e.isEmpty()) {
//            System.out.println("⚠️ Event not found with ID " + eventId);
//            return;
//        }
//
//        EventRequest event = e.get();
//        Optional<Task> tOpt = event.getTasks().stream()
//                .filter(task -> task.getId() == taskId)
//                .findFirst();
//
//        if (tOpt.isEmpty()) {
//            System.out.println("⚠️ Task not found with ID " + taskId + " in Event#" + eventId);
//            return;
//        }
//
//        Task task = tOpt.get();
//
//        // Remove task from its old team if it was assigned
//        if (store.subteams.containsKey(task.getAssignedTo())) {
//            store.subteams.get(task.getAssignedTo()).getTasks().remove(task);
//        }
//
//        // Reassign to new team
//        task.setAssignedTo(newAssignee);
//        task.setStatus("Reassigned to " + newAssignee);
//
//        // Add to new team if valid
//        if (store.subteams.containsKey(newAssignee)) {
//            store.subteams.get(newAssignee).addTask(task);
//        }
//
//        System.out.println("🔄 Task#" + taskId + " reassigned to " + newAssignee + " for Event#" + eventId);
//    }

    /**
     * Update the status of a task (e.g., Pending → In Progress → Completed).
     */
    public void updateTaskStatus(int eventId, int taskId, String status) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .filter(ev -> ev.getStatus() != null && ev.getStatus().contains("OK"))
                .findFirst();

        if (e.isEmpty()) {
            System.out.println("⚠️ Event not found with ID " + eventId);
            return;
        }

        EventRequest event = e.get();
        Optional<Task> tOpt = event.getTasks().stream()
                .filter(task -> task.getId() == taskId)
                .findFirst();

        if (tOpt.isEmpty()) {
            System.out.println("⚠️ Task not found with ID " + taskId + " in Event#" + eventId);
            return;
        }

        Task task = tOpt.get();
        task.setStatus(status);
        System.out.println("✅ Task#" + taskId + " status updated to: " + status);
    }

    /**
     * List all tasks for a specific event.
     */
    public void listTasks(int eventId) {
        store.events.stream()
                .filter(e -> e.getId() == eventId)
                .filter(ev -> ev.getStatus() != null && ev.getStatus().contains("OK"))
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
