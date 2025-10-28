package service;

import model.*;
import java.util.Optional;

public class FinanceService {
    private final DataStore store;

    public FinanceService(DataStore store) {
        this.store = store;
    }

    public void requestFinance(int eventId, String requester, String details) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .findFirst();

        if (e.isPresent()) {
            FinancialRequest f = new FinancialRequest(requester, details);
            e.get().addFinance(f);
            System.out.println("✅ Finance request submitted for Event#" + eventId);
        } else {
            System.out.println("⚠️ Event not found with ID " + eventId);
        }
    }

    public void processFinance(int eventId, int financeId, String status) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .findFirst();

        if (e.isPresent()) {
            for (FinancialRequest f : e.get().getFinances()) {
                if (f.getId() == financeId) {
                    f.setStatus(status);
                    System.out.println("✅ Finance#" + financeId + " for Event#" + eventId + " updated to " + status);
                    return;
                }
            }
            System.out.println("⚠️ Finance ID not found in this event.");
        } else {
            System.out.println("⚠️ Event not found.");
        }
    }

    public void listFinances(int eventId) {
        store.events.stream()
                .filter(e -> e.getId() == eventId)
                .findFirst()
                .ifPresentOrElse(
                        e -> {
                            if (e.getFinances().isEmpty())
                                System.out.println("No finance requests for this event.");
                            else {
                                System.out.println("--- Finance Requests for Event#" + e.getId() + " ---");
                                e.getFinances().forEach(System.out::println);
                            }
                        },
                        () -> System.out.println("⚠️ Event not found.")
                );
    }
}
