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
        eventService.createEvent("Test Client", "Conference");

        assertEquals(1, dataStore.events.size());
        EventRequest event = dataStore.events.get(0);
        assertNotNull(event);

        assertTrue(event.getId() > 0);

        assertEquals("Test Client", event.getClientName());
        assertEquals("Pending", event.getStatus());
    }

    @Test
    @DisplayName("US 1.2: Senior CS Initial Review (List Pending)")
    void testListPendingEventsForSCS() {
        eventService.createEvent("Client A", "Type A"); // Status: Pending
        eventService.createEvent("Client B", "Type B");

        EventRequest eventA = dataStore.events.get(0);
        EventRequest eventB = dataStore.events.get(1);
        eventB.setStatus("OK");

        List<Integer> pendingIds = eventService.listPendingEventsForSCS();

        assertEquals(1, pendingIds.size());

        assertEquals(eventA.getId(), pendingIds.get(0));
    }

    @Test
    @DisplayName("US 1.3: Financial Manager Event Review (List Pending)")
    void testListPendingEventsForFM() {
        eventService.createEvent("Client A", "Type A");

        EventRequest event = dataStore.events.get(0);
        event.setStatus("Approved by SCS → Pending FM Review");

        List<Integer> pendingIds = eventService.listPendingEventsForFM();

        assertEquals(1, pendingIds.size());

        assertEquals(event.getId(), pendingIds.get(0));
    }

    @Test
    @DisplayName("US 1.4: Admin Manager Final Approval (List Pending)")
    void testListPendingEventsForAM() {
        eventService.createEvent("Client A", "Type A");

        EventRequest event = dataStore.events.get(0);
        event.setStatus("Approved by FM → Pending Admin Approval");

        List<Integer> pendingIds = eventService.listPendingEventsForAM();

        assertEquals(1, pendingIds.size());

        assertEquals(event.getId(), pendingIds.get(0));
    }

    @Test
    @DisplayName("US 1.5: Notify Client (List Pending)")
    void testListPendingEventsForFinalSCS() {
        eventService.createEvent("Client A", "Type A");

        EventRequest event = dataStore.events.get(0);
        event.setStatus("Approved by AM → Notify SCS/Client");

        List<Integer> pendingIds = eventService.listPendingEventsForFinalSCS();

        assertEquals(1, pendingIds.size());

        assertEquals(event.getId(), pendingIds.get(0));
    }

    @Test
    @DisplayName("US 1.5: Notify Client (Mark as OK)")
    void testNotifyClientMarkOK() {
        eventService.createEvent("Client A", "Type A");

        EventRequest event = dataStore.events.get(0);
        event.setStatus("Approved by AM → Notify SCS/Client");

        eventService.updateStatus(event.getId(), "OK", "Client Notified");

        assertEquals("OK", event.getStatus());
        assertEquals("Client Notified", event.getNotes());

        List<Integer> pendingIds = eventService.listPendingEventsForFinalSCS();
        assertTrue(pendingIds.isEmpty());
    }
}