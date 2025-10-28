package service;

import model.*;
import java.util.Optional;

public class HRService {
    private final DataStore store;

    public HRService(DataStore store) {
        this.store = store;
    }

    /**
     * Request a new recruitment for a specific event.
     */
    public void requestRecruitment(int eventId, String department, String reason) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .findFirst();

        if (e.isPresent()) {
            RecruitmentRequest r = new RecruitmentRequest(department, reason);
            e.get().addRecruitment(r);
            System.out.println("✅ Recruitment request added for Event#" + eventId);
        } else {
            System.out.println("⚠️ Event not found.");
        }
    }

    /**
     * Process a recruitment request by approving or rejecting it.
     */
    public void processRecruitment(int eventId, int recruitmentId, String newStatus) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .findFirst();

        if (e.isEmpty()) {
            System.out.println("⚠️ Event not found with ID " + eventId);
            return;
        }

        EventRequest event = e.get();
        Optional<RecruitmentRequest> r = event.getRecruitments().stream()
                .filter(req -> req.getId() == recruitmentId)
                .findFirst();

        if (r.isPresent()) {
            r.get().setStatus(newStatus);
            System.out.println("✅ Recruitment#" + recruitmentId + " updated to: " + newStatus);
        } else {
            System.out.println("⚠️ Recruitment not found with ID " + recruitmentId + " in Event#" + eventId);
        }
    }

    /**
     * List all recruitment requests for a specific event.
     */
    public void listRecruitments(int eventId) {
        store.events.stream()
                .filter(e -> e.getId() == eventId)
                .findFirst()
                .ifPresentOrElse(
                        e -> {
                            if (e.getRecruitments().isEmpty())
                                System.out.println("No recruitment requests for this event.");
                            else {
                                System.out.println("--- Recruitment Requests for Event#" + e.getId() + " ---");
                                e.getRecruitments().forEach(System.out::println);
                            }
                        },
                        () -> System.out.println("⚠️ Event not found.")
                );
    }
}
