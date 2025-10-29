package service;

import model.*;
import java.util.*;

public class HRService {
    private final DataStore store;
    private final Scanner sc = new Scanner(System.in);

    public HRService(DataStore store) {
        this.store = store;
    }

    /**
     * Request a new recruitment for a specific event.
     */
    public void requestRecruitment(int eventId, String department, String reason, int numberOfPositions) {
        Optional<EventRequest> e = store.events.stream()
                .filter(ev -> ev.getId() == eventId)
                .findFirst();

        if (e.isPresent()) {
            RecruitmentRequest r = new RecruitmentRequest(department, reason, numberOfPositions);
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
            RecruitmentRequest req = r.get();
            boolean approved = newStatus.equalsIgnoreCase("Approved") ||
                    newStatus.equalsIgnoreCase("Approve") ||
                    newStatus.equalsIgnoreCase("Yes");

            if (approved) {
                System.out.println("✅ Recruitment approved for department: " + req.getDepartment());
                List<String> newMembers = new ArrayList<>();

                for (int i = 1; i <= req.getNumberOfPositions(); i++) {
                    System.out.print("Enter name of new member #" + i + ": ");
                    String member = sc.nextLine().trim();
                    if (!member.isEmpty()) {
                        newMembers.add(member);
//                        get the SubTeam and add member
                        SubTeamRequest team = store.subteams.get(req.getDepartment());
                        if (team != null) {
                            team.addMember(member);
                            System.out.println("✅ Added " + member + " to " + req.getDepartment());
                        } else {
                            System.out.println("⚠️ Department " + req.getDepartment() + " not found in SubTeams.");
                        }
                    }
                }

                req.setStatus("Approved (" + newMembers.size() + " members added)");
                event.setNumberOfWorkers(event.getNumberOfWorkers() + newMembers.size());
                System.out.println("👥 Added " + newMembers.size() + " new members to " + req.getDepartment());
            } else {
                req.setStatus("Rejected");
                System.out.println("❌ Recruitment#" + recruitmentId + " has been rejected.");
            }

            System.out.println("✅ Recruitment#" + recruitmentId + " updated to: " + req.toString());
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
