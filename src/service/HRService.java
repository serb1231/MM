package service;

import model.RecruitmentRequest;
import java.util.List;

public class HRService {
    private final DataStore store;

    public HRService(DataStore store) {
        this.store = store;
    }

    // Called by Production/Service Managers to request new hires or outsourcing
    public void requestRecruitment(String department, String reason) {
        store.recruits.add(new RecruitmentRequest(department, reason));
        System.out.println("✅ Recruitment request created for " + department);
    }

    // Called by HR to approve or reject a recruitment request
    public void processRecruitment(int id, String status) {
        for (RecruitmentRequest r : store.recruits) {
            if (r.getId() == id) {
                r.setStatus(status);
                System.out.println("✅ Recruitment request #" + id + " updated to: " + status);
                return;
            }
        }
        System.out.println("⚠️ No recruitment request found with ID " + id);
    }

    // Lists all recruitment requests (for managers or HR view)
    public void listRecruitments() {
        if (store.recruits.isEmpty()) {
            System.out.println("No recruitment requests available.");
        } else {
            System.out.println("--- Recruitment Requests ---");
            for (RecruitmentRequest r : store.recruits) {
                System.out.println(r);
            }
        }
    }
}
