package service;

import model.EventRequest;
import model.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTestWorkflow {

    private DataStore dataStore;
    private EventService eventService;

    @BeforeEach
    void setUp() {
        dataStore = new DataStore();
        eventService = new EventService(dataStore);
    }

    @Test
    @DisplayName("US 1.1: Create Event Request")
    void testCreateEvent() {
        // Act
        eventService.createEvent("Test Client", "Conference");

        // Assert
        assertEquals(1, dataStore.events.size());
        EventRequest event = dataStore.events.get(0);
        assertNotNull(event);
        assertEquals(1, event.getId());
        assertEquals("Test Client", event.getClientName());
        assertEquals("Pending", event.getStatus());
    }

    @Test
    @DisplayName("US 1.2: Senior CS Initial Review (List Pending)")
    void testListPendingEventsForSCS() {
        // Arrange
        eventService.createEvent("Client A", "Type A"); // Status: Pending
        eventService.createEvent("Client B", "Type B");
        dataStore.events.get(1).setStatus("OK"); // Change status

        // Act
        List<Integer> pendingIds = eventService.listPendingEventsForSCS();

        // Assert
        assertEquals(1, pendingIds.size());
        assertEquals(1, pendingIds.get(0)); // Should only contain ID 1
    }

    @Test
    @DisplayName("US 1.3: Financial Manager Event Review (List Pending)")
    void testListPendingEventsForFM() {
        // Arrange
        eventService.createEvent("Client A", "Type A");
        dataStore.events.get(0).setStatus("Approved by SCS → Pending FM Review");

        // Act
        List<Integer> pendingIds = eventService.listPendingEventsForFM();

        // Assert
        assertEquals(1, pendingIds.size());
        assertEquals(1, pendingIds.get(0));
    }

    @Test
    @DisplayName("US 1.4: Admin Manager Final Approval (List Pending)")
    void testListPendingEventsForAM() {
        // Arrange
        eventService.createEvent("Client A", "Type A");
        dataStore.events.get(0).setStatus("Approved by FM → Pending Admin Approval");

        // Act
        List<Integer> pendingIds = eventService.listPendingEventsForAM();

        // Assert
        assertEquals(1, pendingIds.size());
        assertEquals(1, pendingIds.get(0));
    }

    @Test
    @DisplayName("US 1.5: Notify Client (List Pending)")
    void testListPendingEventsForFinalSCS() {
        // Arrange
        eventService.createEvent("Client A", "Type A");
        dataStore.events.get(0).setStatus("Approved by AM → Notify SCS/Client");

        // Act
        List<Integer> pendingIds = eventService.listPendingEventsForFinalSCS();

        // Assert
        assertEquals(1, pendingIds.size());
        assertEquals(1, pendingIds.get(0));
    }

    @Test
    @DisplayName("US 1.5: Notify Client (Mark as OK)")
    void testNotifyClientMarkOK() {
        // Arrange
        eventService.createEvent("Client A", "Type A");
        EventRequest event = eventService.getEventById(1);
        event.setStatus("Approved by AM → Notify SCS/Client"); // Simulate previous step

        // Act
        // In the UI, this is: event.setStatus("OK");
        eventService.updateStatus(1, "OK", "Client Notified");

        // Assert
        assertEquals("OK", event.getStatus());
        assertEquals("Client Notified", event.getNotes());
        
        // Check that it no longer appears in the pending list
        List<Integer> pendingIds = eventService.listPendingEventsForFinalSCS();
        assertTrue(pendingIds.isEmpty());
    }
}