package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.EmployeesPage;

import java.util.List;

public class EmployeesTest extends BaseTest {

    @Test
    public void verifyEmployeesFlow() {

        page.navigate("http://digy-hrms-quality-fe.s3-website-us-east-1.amazonaws.com/auth/login");

        LoginPage    login = new LoginPage(page);
        EmployeesPage emp  = new EmployeesPage(page);

        // ── 1. LOGIN ──────────────────────────────────────────────────────────
        login.enterEmailAndClickNext("andrew.paul@digylax.com");
        login.enterPasswordAndLogin("Andrew@98");

        // ── 2. NAVIGATE TO EMPLOYEES ──────────────────────────────────────────
        // goToEmployees() clicks the card, waits for networkidle, takes a
        // debug screenshot, and verifies we are on the list page.
        // If the card navigated to a wrong sub-page, it tries direct URL navigation.
        emp.goToEmployees();

        // ── 3. SWITCH TO EMPLOYEE VIEW ────────────────────────────────────────
        emp.switchToEmployeeView();

        // ── 4. FORMER EMPLOYEES TAB ───────────────────────────────────────────
        emp.goToFormerAndWait();

        // ── 5. BACK TO ALL TEAM ───────────────────────────────────────────────
        emp.switchToAllTeam();

        // ── 6. SEARCH VALIDATION ──────────────────────────────────────────────
        emp.searchMultipleEmployees(List.of(
                "andrew", "hari", "karthi", "praveen", "krishna"
        ));
        page.waitForTimeout(2000);

        // ── 7. RESET ──────────────────────────────────────────────────────────
        emp.switchToAllTeam();

        // ── 8. SORT VALIDATION ────────────────────────────────────────────────
        emp.applyAscendingSortAndValidate();
        emp.applyDescendingSortAndValidate();

        // ── 9. TABLE VIEW ─────────────────────────────────────────────────────
        emp.switchToTableView();
        page.waitForTimeout(2000);

        // ── 10. FILTER VALIDATION ─────────────────────────────────────────────
        emp.filterByFirstNames(List.of(
                "andrew", "hari", "karthi", "praveen", "krishna"
        ));

        // ── 11. PAGINATION VALIDATION ─────────────────────────────────────────
        emp.verifyPaginationAndData();

        // ── 12. FORMER ISOLATION VALIDATION ───────────────────────────────────
        emp.verifyFormerNotInAllTeam();

        System.out.println("\n🎉 ALL VALIDATIONS PASSED 🎉\n");
    }
}
