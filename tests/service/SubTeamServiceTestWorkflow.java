package service;

import model.EventService;
import model.SubTeamRequest;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class SubTeamServiceTestWorkflow {

    private DataStore dataStore;
    private SubTeamService subTeamService;
    private TaskService taskService;
    private EventService eventService;

    @BeforeEach
    void setUp() {
        dataStore = new DataStore();
        subTeamService = new SubTeamService(dataStore);
        taskService = new TaskService(dataStore);
        eventService = new EventService(dataStore);

        // Helper to create an "OK" event for task assignment
        eventService.createEvent("Client", "Event");
        eventService.getEventById(1).setStatus("OK");
    }

    @Test
    @DisplayName("US 4.1: View Team Tasks")
    void testViewTeamTasks() {
        // Arrange
        taskService.createTask(1, "Task 1", "Logistics");
        taskService.createTask(1, "Task 2", "Logistics");

        // Act
        SubTeamRequest team = dataStore.subteams.get("Logistics");

        // Assert
        assertEquals(2, team.getTasks().size());
    }

    @Test
    @DisplayName("US 4.2: Respond to Task (Accept)")
    void testRespondToTask_Accept() {
        // Arrange
        taskService.createTask(1, "Task 1", "Logistics");
        Task task = dataStore.subteams.get("Logistics").getTasks().get(0);
        assertEquals("Pending", task.getStatus()); // Pre-condition

        // Act
        subTeamService.respondToTask("Logistics", task.getId(), true, "");

        // Assert
        assertEquals("Accepted ✅", task.getStatus());
    }

    @Test
    @DisplayName("US 4.2: Respond to Task (Reject)")
    void testRespondToTask_Reject() {
        // Arrange
        taskService.createTask(1, "Task 1", "Logistics");
        Task task = dataStore.subteams.get("Logistics").getTasks().get(0);
        assertEquals("Pending", task.getStatus());

        // Act
        subTeamService.respondToTask("Logistics", task.getId(), false, "No trucks available");

        // Assert
        assertEquals("Rejected ❌ (No trucks available)", task.getStatus());
    }

    @Test
    @DisplayName("US 4.3: View Team Members")
    void testViewTeamMembers() {
        // Arrange
        subTeamService.addMember("Logistics", "Driver 1");
        subTeamService.addMember("Logistics", "Driver 2");

        // Act
        SubTeamRequest team = dataStore.subteams.get("Logistics");

        // Assert
        assertEquals(2, team.getMembers().size());
        assertTrue(team.getMembers().contains("Driver 1"));
    }
}