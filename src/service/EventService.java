package service;

import model.*;

public class EventService {
    private DataStore store;

    public EventService(DataStore store) { this.store = store; }

    public void createEvent(String clientName, String type) {
        store.events.add(new EventRequest(clientName, type));
    }

    public void listEvents() {
        if (store.events.isEmpty()) System.out.println("No events yet.");
        else store.events.forEach(System.out::println);
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
