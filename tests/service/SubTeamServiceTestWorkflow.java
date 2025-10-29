package service;

import model.EventRequest; // 导入 EventRequest
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

    private EventRequest event;

    @BeforeEach
    void setUp() {
        dataStore = new DataStore();
        subTeamService = new SubTeamService(dataStore);
        taskService = new TaskService(dataStore);
        eventService = new EventService(dataStore);

        eventService.createEvent("Client", "Event");
        this.event = dataStore.events.get(0);
        this.event.setStatus("OK");
    }

    @Test
    @DisplayName("US 4.1: View Team Tasks")
    void testViewTeamTasks() {
        taskService.createTask(this.event.getId(), "Task 1", "Logistics");
        taskService.createTask(this.event.getId(), "Task 2", "Logistics");

        SubTeamRequest team = dataStore.subteams.get("Logistics");

        assertEquals(2, team.getTasks().size());
    }

    @Test
    @DisplayName("US 4.2: Respond to Task (Accept)")
    void testRespondToTask_Accept() {
        taskService.createTask(this.event.getId(), "Task 1", "Logistics");
        Task task = dataStore.subteams.get("Logistics").getTasks().get(0);
        assertEquals("Pending", task.getStatus());

        subTeamService.respondToTask("Logistics", task.getId(), true, "");

        assertEquals("Accepted", task.getStatus());
    }

    @Test
    @DisplayName("US 4.2: Respond to Task (Reject)")
    void testRespondToTask_Reject() {
        taskService.createTask(this.event.getId(), "Task 1", "Logistics");
        Task task = dataStore.subteams.get("Logistics").getTasks().get(0);
        assertEquals("Pending", task.getStatus());

        subTeamService.respondToTask("Logistics", task.getId(), false, "No trucks available");

        assertEquals("Rejected (No trucks available)", task.getStatus());
    }

    @Test
    @DisplayName("US 4.3: View Team Members")
    void testViewTeamMembers() {
        subTeamService.addMember("Logistics", "Driver 1");
        subTeamService.addMember("Logistics", "Driver 2");

        SubTeamRequest team = dataStore.subteams.get("Logistics");

        assertEquals(2, team.getMembers().size());
        assertTrue(team.getMembers().contains("Driver 1"));
    }
}