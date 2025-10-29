package service;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentProcessTestWorkflow {

    private DataStore dataStore;
    private EventService eventService;
    private HRService hrService;
    private FinanceService financeService;
    private SubTeamService subTeamService;

    // To mock System.in
    private final InputStream originalIn = System.in;
    private ByteArrayInputStream testIn;

    @BeforeEach
    void setUp() {
        dataStore = new DataStore();
        eventService = new EventService(dataStore);
        hrService = new HRService(dataStore);
        financeService = new FinanceService(dataStore);
        subTeamService = new SubTeamService(dataStore);
    }

    @AfterEach
    void tearDown() {
        // Restore System.in
        System.setIn(originalIn);
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    /** Helper to create an "OK" event */
    private EventRequest createOKEvent() {
        eventService.createEvent("Client", "Event");
        EventRequest event = eventService.getAllEvents().get(dataStore.events.size() - 1);
        event.setStatus("OK");
        return event;
    }

    @Test
    @DisplayName("US 3.1: Process Recruitment Request (Approve)")
    void testProcessRecruitment_Approve() {
        // Arrange
        EventRequest event = dataStore.events.getLast();
        hrService.requestRecruitment(event.getId(), "Security", "Need 2 guards", 2);
        RecruitmentRequest req = event.getRecruitments().get(0);
        
        // Mock user input for the 2 new member names
        String input = "New Guard 1\nNew Guard 2\n";
        provideInput(input);

        // Act
        hrService.processRecruitment(event.getId(), req.getId(), "Approve");

        // Assert
        SubTeamRequest team = dataStore.subteams.get("Security");
        assertEquals(2, team.getMembers().size());
        assertEquals("New Guard 1", team.getMembers().get(0));
        assertEquals("New Guard 2", team.getMembers().get(1));
        assertEquals(2, event.getNumberOfWorkers());
        assertTrue(req.getStatus().contains("Approved"));
    }

    @Test
    @DisplayName("US 3.1: Process Recruitment Request (Reject)")
    void testProcessRecruitment_Reject() {
        // Arrange
        EventRequest event = createOKEvent();
        hrService.requestRecruitment(event.getId(), "Security", "Need 2 guards", 2);
        RecruitmentRequest req = event.getRecruitments().get(0);

        // Act
        hrService.processRecruitment(event.getId(), req.getId(), "Reject");

        // Assert
        SubTeamRequest team = dataStore.subteams.get("Security");
        assertTrue(team.getMembers().isEmpty());
        assertEquals(0, event.getNumberOfWorkers());
        assertEquals("Rejected", req.getStatus());
    }

    @Test
    @DisplayName("US 3.2: Manage Sub-Team Members")
    void testManageSubTeamMembers() {
        // Arrange
        SubTeamRequest team = dataStore.subteams.get("Decoration");
        assertTrue(team.getMembers().isEmpty());

        // Act
        subTeamService.addMember("Decoration", "Florist");

        // Assert
        assertEquals(1, team.getMembers().size());
        assertEquals("Florist", team.getMembers().get(0));
    }

    @Test
    @DisplayName("US 3.3: Process Financial Request (Approve)")
    void testProcessFinancialRequest_Approve() {
        // Arrange
        EventRequest event = createOKEvent();
        financeService.requestFinance(event.getId(), "PM", "Need $500");
        FinancialRequest req = event.getFinances().get(0);
        assertEquals(0.0, event.getBudget()); // Pre-condition

        // Mock user input for the amount
        String input = "500.0\n";
        provideInput(input);

        // Act
        financeService.processFinance(event.getId(), req.getId(), "Approved");

        // Assert
        assertEquals(500.0, event.getBudget());
        assertEquals("Approved - $500.0 added", req.getStatus());
    }

    @Test
    @DisplayName("US 3.3: Process Financial Request (Reject)")
    void testProcessFinancialRequest_Reject() {
        // Arrange
        EventRequest event = createOKEvent();
        financeService.requestFinance(event.getId(), "PM", "Need $500");
        FinancialRequest req = event.getFinances().get(0);
        assertEquals(0.0, event.getBudget());

        // Act
        financeService.processFinance(event.getId(), req.getId(), "Rejected");

        // Assert
        assertEquals(0.0, event.getBudget()); // Budget remains 0
        assertEquals("Rejected", req.getStatus());
    }

    @Test
    @DisplayName("US 3.3: Process Financial Request (Approve, Invalid Amount)")
    void testProcessFinancialRequest_Approve_InvalidAmount() {
        // Arrange
        EventRequest event = createOKEvent();
        financeService.requestFinance(event.getId(), "PM", "Need $500");
        FinancialRequest req = event.getFinances().get(0);

        // Mock invalid user input
        String input = "not-a-number\n";
        provideInput(input);

        // Act
        financeService.processFinance(event.getId(), req.getId(), "Approved");

        // Assert
        assertEquals(0.0, event.getBudget()); // Budget remains 0
        // Status remains pending because the method returns early
        assertEquals("Pending Finance", req.getStatus());
    }
}