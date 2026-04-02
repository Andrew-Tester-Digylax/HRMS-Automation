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
        emp.goToEmployees();

        // ── 3. OPEN FORMER EMPLOYEES TAB ──────────────────────────────────────
        emp.goToFormerAndWait();

        // ── 4. BACK TO ALL TEAM ───────────────────────────────────────────────
        emp.switchToAllTeam();

        // ── 5. SEARCH ─────────────────────────────────────────────────────────
        emp.searchMultipleEmployees(List.of(
                "andrew", "hari", "karthi", "praveen", "krishna"
        ));
        page.waitForTimeout(2000);

        // ── 6. RESET ──────────────────────────────────────────────────────────
        emp.switchToAllTeam();

        // ── 7. SORT ───────────────────────────────────────────────────────────
        emp.applyAscendingSortAndValidate();
        emp.applyDescendingSortAndValidate();

        // ── 8. TABLE VIEW ─────────────────────────────────────────────────────
        // Toggle: "//span[@class='me-2']//*[name()='svg']"
        // Item:   "//a[normalize-space()='Table View']"
        emp.switchToTableView();
        page.waitForTimeout(2000);

        // ── 9. FILTER (AG Grid floating filter) ───────────────────────────────
        emp.filterByFirstNames(List.of(
                "andrew", "hari", "karthi", "praveen", "krishna"
        ));

        // ── 10. SWITCH BACK TO EMPLOYEE VIEW ──────────────────────────────────
        // Toggle: "//span[@class='me-2']//*[name()='svg']"
        // Item:   "//a[normalize-space()='Employee View']"
        emp.switchToEmployeeView();
        page.waitForTimeout(2000);

        // ── 11. ALL TEAM ──────────────────────────────────────────────────────
        emp.switchToAllTeam();

        // ── 12. PAGINATION VALIDATION ─────────────────────────────────────────
        // • Reads "1 to 10 of 15" → total=15
        // • Scrolls down → verifies 10 records on page 1
        // • Clicks  "//span[@class='ag-icon ag-icon-next']"
        // • Scrolls down → verifies 5 records on page 2
        // • Checks no duplicates between pages
        // • Asserts 10 + 5 = 15
        emp.verifyPaginationAndData();

        // ── 13. FORMER ISOLATION ──────────────────────────────────────────────
        // • Collects All Team names (all pages)
        // • Opens "//button[@id='pill-tab-tab-resigned']"
        // • Collects Former names (all pages)
        // • Fails + screenshot if any name appears in both
        emp.verifyFormerNotInAllTeam();

        System.out.println("\n🎉 ALL VALIDATIONS PASSED 🎉\n");
    }
}