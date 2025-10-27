==================================================
README - Swedish Event Planners (SEP) System
Course: ID2207 - Modern Methods in Software Engineering
Methodology: Extreme Programming (XP)
Language: Java (Terminal UI)
==================================================

OVERVIEW
---------
This program implements the internal management system
for the fictional company "Swedish Event Planners (SEP)".
It simulates the organization's workflows between departments
using a terminal-based user interface and role-based access.

--------------------------------------------------
HOW TO RUN
--------------------------------------------------

1. Open the project.
2. Make sure your folder structure looks like:
   src/
     model/
     service/
     ui/
     Main.java
3. Right-click Main.java → Run 'Main.main()'

--------------------------------------------------
DEFAULT USERS
--------------------------------------------------

Username    Password    Role
--------------------------------
alice       1234        CustomerService
janet       1234        SeniorCS
bob         1234        FinancialManager
mike        1234        AdminManager
jack        1234        ProductionManager
emma        1234        ServiceManager
simon       1234        HR
vp          1234        VicePresident

--------------------------------------------------
SYSTEM WORKFLOWS
--------------------------------------------------

1. EVENT REQUEST WORKFLOW
   CustomerService -> SeniorCS -> FinancialManager -> AdminManager
   - Create, review, approve, and finalize event requests.

2. TASK DISTRIBUTION WORKFLOW
   ProductionManager / ServiceManager assign tasks to subteams.

3. RECRUITMENT WORKFLOW
   Managers request recruitment -> HR processes approvals.

4. FINANCIAL REQUEST WORKFLOW
   Managers request budgets -> FinancialManager processes them.

5. REPORTS
   VicePresident can view all events, tasks, recruitment, and finance data.

--------------------------------------------------
BASIC USAGE
--------------------------------------------------

1. Run the program.
2. Enter username and password.
3. Use the numbered menu options according to your role.

Example:
Username: alice
Password: 1234

Welcome, alice (CustomerService)
1. Create Event
2. View Events
3. Logout

--------------------------------------------------
DATA STORAGE
--------------------------------------------------

All data (users, events, tasks, recruitment, finance)
is stored in memory only (no files or database).
Data resets when the program exits.

--------------------------------------------------
TESTING
--------------------------------------------------

Unit Tests (TDD):
- EventService
- HRService
- FinanceService

Acceptance Tests:
1. Event request workflow (creation to approval)
2. Recruitment workflow (request to HR approval)

--------------------------------------------------
NOTES
--------------------------------------------------

- The program allows role-based access to menus.
- Status updates (Approved, Rejected, etc.) can be entered manually.
- It is OK if data disappears when the program stops.


