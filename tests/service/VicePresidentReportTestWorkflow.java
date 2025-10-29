package service;

import model.EventRequest;
import model.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class VicePresidentReportTestWorkflow {

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

    @Test
    @DisplayName("US 5.1: View All Reports by Event")
    void testViewAllReportsByEvent() {
        // Arrange
        // 1. Create an "OK" event
        eventService.createEvent("VP Test Client", "VP Event");
        EventRequest event = eventService.getEventById(1);
        event.setStatus("OK");

        // 2. Add one of each request type
        taskService.createTask(event.getId(), "VP Task", "Catering");
        hrService.requestRecruitment(event.getId(), "Security", "VP HR Request", 3);
        financeService.requestFinance(event.getId(), "VP", "VP Finance Request");

        // Act
        // (In the UI, the VP would select Event 1)
        EventRequest fetchedEvent = eventService.getEventById(1);

        // Assert
        // The "report" is just viewing all lists. We assert all lists are populated.
        assertNotNull(fetchedEvent);
        assertEquals(1, fetchedEvent.getTasks().size());
        assertEquals(1, fetchedEvent.getRecruitments().size());
        assertEquals(1, fetchedEvent.getFinances().size());

        assertEquals("VP Task", fetchedEvent.getTasks().get(0).toString());
        assertEquals(3, fetchedEvent.getRecruitments().get(0).getNumberOfPositions());
        assertTrue(fetchedEvent.getFinances().get(0).toString().contains("VP Finance Request"));
    }
}