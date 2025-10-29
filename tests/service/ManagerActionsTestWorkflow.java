package service;

import model.EventRequest;
import model.EventService;
import model.SubTeamRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ManagerActionsTestWorkflow {

    private DataStore dataStore;
    private EventService eventService;
    private TaskService taskService;
    private HRService hrService;
    private FinanceService financeService;

    @BeforeEach
    void setUp() {
        dataStore = new DataStore();
        eventService = new EventService(dataStore);
        taskService = new TaskService(dataStore);
        hrService = new HRService(dataStore);
        financeService = new FinanceService(dataStore);
    }

    private EventRequest createOKEvent() {
        eventService.createEvent("OK Client", "OK Event");

        EventRequest event = dataStore.events.get(0);

        event.setStatus("OK");
        return event;
    }

    @Test
    @DisplayName("US 2.1: Assign Task to Sub-Team")
    void testAssignTaskToSubTeam() {
        EventRequest event = createOKEvent();
        SubTeamRequest cateringTeam = dataStore.subteams.get("Catering");
        assertTrue(cateringTeam.getTasks().isEmpty()); // Pre-condition

        taskService.createTask(event.getId(), "Prepare buffet", "Catering");

        assertEquals(1, event.getTasks().size());
        assertEquals(1, cateringTeam.getTasks().size());

        assertTrue(event.getTasks().get(0).toString().contains("Prepare buffet"));
    }

    @Test
    @DisplayName("US 2.1: Assign Task Fails for Non-OK Event")
    void testAssignTaskFailsForPendingEvent() {
        eventService.createEvent("Pending Client", "Pending Event"); // Status is "Pending"

        EventRequest event = dataStore.events.get(0);

        SubTeamRequest cateringTeam = dataStore.subteams.get("Catering");

        taskService.createTask(event.getId(), "This should not work", "Catering");

        assertTrue(event.getTasks().isEmpty());
        assertTrue(cateringTeam.getTasks().isEmpty());
    }

    @Test
    @DisplayName("US 2.2: Request Recruitment")
    void testRequestRecruitment() {
        EventRequest event = createOKEvent();
        assertTrue(event.getRecruitments().isEmpty());

        hrService.requestRecruitment(event.getId(), "Logistics", "Need 5 drivers", 5);

        assertEquals(1, event.getRecruitments().size());
        assertEquals("Logistics", event.getRecruitments().get(0).getDepartment());
        assertEquals(5, event.getRecruitments().get(0).getNumberOfPositions());

        assertEquals("Pending HR", event.getRecruitments().get(0).getStatus());
    }

    @Test
    @DisplayName("US 2.3: Request Financial Adjustment")
    void testRequestFinancialAdjustment() {
        EventRequest event = createOKEvent();
        assertTrue(event.getFinances().isEmpty());

        String details = "New lighting rig [Requested $1500]";
        financeService.requestFinance(event.getId(), "ProductionManager", details);

        assertEquals(1, event.getFinances().size());

        assertEquals("Pending Finance", event.getFinances().get(0).getStatus());
    }
}