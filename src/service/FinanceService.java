package service;

import model.*;
import java.util.Optional;
import java.util.Scanner;

public class FinanceService {
    private final DataStore store;

    public FinanceService(DataStore store) {
        this.store = store;
    }

    public void requestFinance(int eventId, String requester, String details) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .filter(ev -> ev.getStatus() != null && ev.getStatus().contains("OK"))
                .findFirst();

        if (e.isPresent()) {
            FinancialRequest f = new FinancialRequest(requester, details);
            e.get().addFinance(f);
            System.out.println("Finance request submitted for Event#" + eventId);
        } else {
            System.out.println("Event not found with ID " + eventId);
        }
    }

    public void processFinance(int eventId, int financeId, String decision) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .filter(ev -> ev.getStatus() != null && ev.getStatus().contains("OK"))
                .findFirst();

        if (e.isPresent()) {
            EventRequest event = e.get();
            for (FinancialRequest f : event.getFinances()) {
                if (f.getId() == financeId) {

                    boolean approved = decision.equalsIgnoreCase("Approved") ||
                            decision.equalsIgnoreCase("Approve") ||
                            decision.equalsIgnoreCase("Yes") ||
                            decision.equalsIgnoreCase("True");

                    if (approved) {
                        Scanner sc = new Scanner(System.in);
                        System.out.print("Enter the approved amount to add to event budget: $");
                        double amount = 0;
                        try {
                            amount = Double.parseDouble(sc.nextLine());
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid number. No budget updated.");
                            return;
                        }

                        double newBudget = event.getBudget() + amount;
                        event.setBudget(newBudget);
                        f.setStatus("Approved - $" + amount + " added");
                        System.out.println("Approved! Event#" + eventId + " budget increased by $" + amount);
                        System.out.println("New budget: $" + newBudget);
                    } else {
                        f.setStatus("Rejected");
                        System.out.println("Finance request #" + financeId + " has been rejected.");
                    }
                    return;
                }
            }
            System.out.println("Finance ID not found in this event.");
        } else {
            System.out.println("Event not found.");
        }
    }

    public void listFinances(int eventId) {
        store.events.stream()
                .filter(e -> e.getId() == eventId)
                .filter(e -> e.getStatus() != null && e.getStatus().contains("OK"))
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
                        () -> System.out.println("Event not found.")
                );
    }
}
