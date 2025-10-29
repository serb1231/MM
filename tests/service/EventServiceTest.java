package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {

    private DataStore store;
    private EventService eventService;

    @BeforeEach
    void setUp() {
        store = new DataStore();
        eventService = new EventService(store);
    }

    @Test
    void createEvent_CreatesIt() {
        eventService.createEvent("Serb", "Concert");
        assertEquals(1, store.events.size());
        assertEquals("Serb", store.events.getFirst().getClientName());
        assertEquals("Concert", store.events.getFirst().getEventType());
    }

    @Test
    void listEvents_ReturnsOKEvents() {
        EventRequest e1 = new EventRequest("Client1", "Birthday");
        e1.setStatus("OK");
        EventRequest e2 = new EventRequest("Client2", "Conference");
        e2.setStatus("Pending");

        store.events.add(e1);
        store.events.add(e2);

        List<Integer> result = eventService.listEvents();
        assertEquals(1, result.size());
        assertEquals(e1.getId(), result.getFirst());
    }

    @Test
    void listPendingEventsForSCS_ReturnsOnlyPendingEvents() {
        EventRequest e1 = new EventRequest("Client1", "Wedding");
        e1.setStatus("Pending");
        EventRequest e2 = new EventRequest("Client2", "Wedding");
        e2.setStatus("Approved by SCS → Pending FM Review");
        store.events.add(e1);
        store.events.add(e2);

        List<Integer> result = eventService.listPendingEventsForSCS();
        assertEquals(1, result.size());
        assertEquals(e1.getId(), result.getFirst());
    }

    @Test
    void listPendingEventsForFM_ReturnsCorrectEvents() {
        EventRequest e = new EventRequest("Client", "Event");
        e.setStatus("Approved by SCS → Pending FM Review");
        store.events.add(e);

        List<Integer> result = eventService.listPendingEventsForFM();
        assertEquals(1, result.size());
        assertEquals(e.getId(), result.getFirst());
    }

    @Test
    void listPendingEventsForFinalSCS_ReturnsCorrectEvents() {
        EventRequest e = new EventRequest("Client", "Event");
        e.setStatus("Approved by AM → Notify SCS/Client");
        store.events.add(e);

        List<Integer> result = eventService.listPendingEventsForFinalSCS();
        assertEquals(1, result.size());
        assertEquals(e.getId(), result.getFirst());
    }

    @Test
    void listPendingEventsForAM_ReturnsCorrectEvents() {
        EventRequest e = new EventRequest("Client", "Event");
        e.setStatus("Approved by FM → Pending Admin Approval");
        store.events.add(e);

        List<Integer> result = eventService.listPendingEventsForAM();
        assertEquals(1, result.size());
        assertEquals(e.getId(), result.getFirst());
    }

    @Test
    void updateStatus_UpdatesEventCorrectly() {
        EventRequest e = new EventRequest("Client", "Event");
        store.events.add(e);

        eventService.updateStatus(e.getId(), "Approved", "Looks good");
        assertEquals("Approved", e.getStatus());
        assertEquals("Looks good", e.getNotes());
    }

    @Test
    void getEventById_ReturnsEventIfExists() {
        EventRequest e = new EventRequest("Client", "Event");
        store.events.add(e);

        EventRequest found = eventService.getEventById(e.getId());
        assertNotNull(found);
        assertEquals(e.getId(), found.getId());
    }

    @Test
    void getEventById_ReturnsNullIfNotFound() {
        EventRequest found = eventService.getEventById(50);
        assertNull(found);
    }

    @Test
    void getAllEvents_ReturnsAllStoredEvents() {
        store.events.add(new EventRequest("Client1", "Type1"));
        store.events.add(new EventRequest("Client2", "Type2"));

        List<EventRequest> result = eventService.getAllEvents();
        assertEquals(2, result.size());
    }
}
