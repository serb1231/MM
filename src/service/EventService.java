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

//    return a list of event id's that exist and are pending
    public List<Integer> listPendingEventsForSCS() {
        boolean found = false;
        List<Integer> list = new ArrayList<>();
        for (EventRequest e : store.events) {
            if (e.getStatus().equalsIgnoreCase("Pending")) {
                System.out.println(e);
                list.add(e.getId());
                found = true;
            }
        }
        if (!found) System.out.println("No pending events.");
        return list;
    }

    public List<Integer> listPendingEventsForFM() {
        boolean found = false;
        List<Integer> list = new ArrayList<>();
        for (EventRequest e : store.events) {
            if (e.getStatus().equalsIgnoreCase("Approved by SCS → Pending FM Review")) {
                System.out.println(e);
                found = true;
                list.add(e.getId());
            }
        }
        if (!found) System.out.println("No events approved by SCS.");
        return list;
    }

    public List<Integer> listPendingEventsForFinalSCS() {
        boolean found = false;
        List<Integer> list = new ArrayList<>();
        for (EventRequest e : store.events) {
            if (e.getStatus().equalsIgnoreCase("Approved by AM → Notify SCS/Client")) {
                System.out.println(e);
                found = true;
                list.add(e.getId());
            }
        }
        if (!found) System.out.println("No events approved by SCS.");
        return list;
    }

    public List<Integer> listPendingEventsForAM() {
        boolean found = false;
        List<Integer> list = new ArrayList<>();
        for (EventRequest e : store.events) {
            if (e.getStatus().equalsIgnoreCase("Approved by FM → Pending Admin Approval")) {
                System.out.println(e);
                found = true;
                list.add(e.getId());
            }
        }
        if (!found) System.out.println("No events approved by SCS.");
        return list;
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
