package service;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class EventService {
    private DataStore store;

    public EventService(DataStore store) { this.store = store; }

    public void createEvent(String clientName, String type) {
        store.events.add(new EventRequest(clientName, type));
    }

    public void listAllEvents() {
        if (store.events.isEmpty()) {
            System.out.println("No events found.");
            return;
        }
        System.out.println("\n--- All Events ---");
        for (EventRequest e : store.events) {
            System.out.println(e);
        }
    }

    public List<Integer> listEvents() {
        boolean found = false;
        System.out.println("\n--- In Work Events ---");
        List<Integer> list = new ArrayList<>();
        for (EventRequest e : store.events) {
            if (e.getStatus().equalsIgnoreCase("OK")) {
                System.out.println(e);
                found = true;
                list.add(e.getId());
            }
        }
        if (!found) {
            System.out.println("No events in work found.");
        }
        return list;
    }

    public void listPendingEventsForSCS() {
        boolean found = false;
        for (EventRequest e : store.events) {
            if (e.getStatus().equalsIgnoreCase("Pending")) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) System.out.println("No pending events.");
    }

    public void listPendingEventsForFM() {
        boolean found = false;
        for (EventRequest e : store.events) {
            if (e.getStatus().equalsIgnoreCase("Approved by SCS → Pending FM Review")) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) System.out.println("No events approved by SCS.");
    }

    public void listPendingEventsForFinalSCS() {
        boolean found = false;
        for (EventRequest e : store.events) {
            if (e.getStatus().equalsIgnoreCase("Approved by AM → Notify SCS/Client")) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) System.out.println("No events approved by SCS.");
    }

    public void listPendingEventsForAM() {
        boolean found = false;
        for (EventRequest e : store.events) {
            if (e.getStatus().equalsIgnoreCase("Approved by FM → Pending Admin Approval")) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) System.out.println("No events approved by SCS.");
    }

    public void updateStatus(int id, String status, String notes) {
        for (EventRequest e : store.events)
            if (e.getId() == id) {
                e.setStatus(status);
                e.setNotes(notes);
            }
    }

//    get events
    public EventRequest getEventById(int id) {
        for (EventRequest e : store.events) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

//    return all events
    public java.util.List<EventRequest> getAllEvents() {
        return store.events;
    }
}
