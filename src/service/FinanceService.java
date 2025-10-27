package service;

import model.FinancialRequest;
import java.util.List;

public class FinanceService {
    private final DataStore store;

    public FinanceService(DataStore store) {
        this.store = store;
    }

    // Called by managers (Production/Service) to request extra budget or financial changes
    public void requestFinance(String requester, String details) {
        store.finances.add(new FinancialRequest(requester, details));
        System.out.println("✅ Finance request submitted by " + requester);
    }

    // Called by the Financial Manager to approve or reject requests
    public void processFinance(int id, String status) {
        for (FinancialRequest f : store.finances) {
            if (f.getId() == id) {
                f.setStatus(status);
                System.out.println("✅ Finance request #" + id + " updated to: " + status);
                return;
            }
        }
        System.out.println("⚠️ No financial request found with ID " + id);
    }

    // Lists all finance requests (for review or reporting)
    public void listFinances() {
        if (store.finances.isEmpty()) {
            System.out.println("No financial requests available.");
        } else {
            System.out.println("--- Financial Requests ---");
            for (FinancialRequest f : store.finances) {
                System.out.println(f);
            }
        }
    }
}
