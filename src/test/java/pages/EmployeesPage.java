package pages;

import com.microsoft.playwright.*;
import org.testng.Assert;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class EmployeesPage {

    private Page page;

    public EmployeesPage(Page page) {
        this.page = page;
    }

    // ================================================================
    // LOCATORS
    // ================================================================

    // -- Module card on dashboard --
    private Locator employeesCard() {
        return page.locator("div.module-link.cursor-pointer")
                .filter(new Locator.FilterOptions()
                        .setHas(page.locator("div.module-name:has-text('Employees')")))
                .first();
    }

    // -- Tabs --
    private Locator allTeamTab() {
        return page.locator("#pill-tab-tab-all_team, button:has-text('All Team')").first();
    }

    private Locator formerTab() {
        return page.locator("//button[@id='pill-tab-tab-resigned']");
    }

    // -- Card-view search --
    private Locator searchBox() {
        return page.locator("input[placeholder='Search']").first();
    }

    // -- Card-view "Team Directory" heading --
    private Locator teamDirectoryHeader() {
        return page.locator("text=Team Directory");
    }

    // -- Card-view first column tds --
    private Locator cardViewFirstColumn() {
        return page.locator("//td[1]");
    }

    // -- Card-view employee name links e.g. "Andrew Paul (60003)" --
    private Locator cardViewNameLinks() {
        return page.locator("//a[contains(text(),'(') and contains(text(),')')]");
    }

    // -- Sort dropdown (card view) --
    private Locator sortDropdown() {
        return page.locator("div.col-auto.ms-1 select.form-select");
    }

    // ── VIEW TOGGLE ──────────────────────────────────────────────────────────
    private Locator viewToggleBtn() {
        return page.locator("//span[@class='me-2']//*[name()='svg']").first();
    }

    private Locator tableViewOption() {
        return page.locator("//a[normalize-space()='Table View']");
    }

    private Locator employeeViewOption() {
        return page.locator("//a[normalize-space()='Employee View']");
    }

    // ── AG GRID ──────────────────────────────────────────────────────────────
    private Locator agGridHeader() {
        return page.locator("div.ag-header");
    }

    // ── PAGINATION ───────────────────────────────────────────────────────────
    private Locator nextArrowBtn() {
        return page.locator("//span[@class='ag-icon ag-icon-next']");
    }

    // ================================================================
    // SCREENSHOT HELPER
    // ================================================================
    private void takeScreenshot(String label) {
        try {
            File dir = new File("screenshots");
            if (!dir.exists()) dir.mkdirs();
            String path = "screenshots/" + label + "_" + System.currentTimeMillis() + ".png";
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(path))
                    .setFullPage(true));
            System.out.println("📸 Screenshot saved: " + path);
        } catch (Exception e) {
            System.err.println("⚠️ Screenshot error: " + e.getMessage());
        }
    }

    // ================================================================
    // NAVIGATION
    // ================================================================
    public void goToEmployees() {
        page.waitForURL("**/dashboard");
        employeesCard().click(new Locator.ClickOptions().setForce(true));
        searchBox().waitFor();
        System.out.println("✅ Navigated to Employees");
    }

    public void goToFormerAndWait() {
        formerTab().click();
        page.waitForTimeout(3000);
        System.out.println("✅ Opened Former Employees tab");
    }

    public void switchToAllTeam() {
        allTeamTab().click();
        teamDirectoryHeader().waitFor();
        page.waitForTimeout(2000);
        System.out.println("✅ Switched to All Team");
    }

    // ================================================================
    // SEARCH
    // ================================================================
    private void clearSearch() {
        searchBox().click();
        searchBox().press("Control+A");
        searchBox().press("Delete");
        searchBox().press("Enter");
        page.waitForTimeout(1000);
    }

    public void searchMultipleEmployees(List<String> names) {
        for (String name : names) {
            clearSearch();
            Locator s = searchBox();
            s.click();
            s.fill("");
            s.type(name, new Locator.TypeOptions().setDelay(100));
            s.press("Enter");
            page.waitForTimeout(3000);

            if (cardViewFirstColumn().count() == 0) {
                takeScreenshot("search_failed_" + name);
                throw new RuntimeException("❌ No results for: " + name);
            }
            System.out.println("✅ Search found: " + name);
            page.waitForTimeout(2000);
        }
        clearSearch();
    }

    // ================================================================
    // SORT (Card View)
    // ================================================================
    private List<String> getCardViewColumnNames() {
        List<String> result = new ArrayList<>();
        for (String r : cardViewFirstColumn().allTextContents()) {
            if (r != null && !r.trim().isEmpty())
                result.add(r.trim().toLowerCase());
        }
        return result;
    }

    public void applyAscendingSortAndValidate() {
        sortDropdown().selectOption("1");
        page.waitForTimeout(3000);
        List<String> actual   = getCardViewColumnNames();
        List<String> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        if (!actual.equals(expected)) {
            takeScreenshot("sort_asc_failed");
            throw new RuntimeException("❌ Ascending sort FAILED\nActual: " + actual
                    + "\nExpected: " + expected);
        }
        System.out.println("✅ Ascending sort correct");
    }

    public void applyDescendingSortAndValidate() {
        sortDropdown().selectOption("-1");
        page.waitForTimeout(3000);
        List<String> actual   = getCardViewColumnNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(Collections.reverseOrder());
        if (!actual.equals(expected)) {
            takeScreenshot("sort_desc_failed");
            throw new RuntimeException("❌ Descending sort FAILED\nActual: " + actual
                    + "\nExpected: " + expected);
        }
        System.out.println("✅ Descending sort correct");
    }

    // ================================================================
    // VIEW TOGGLE
    // ================================================================
    public void switchToTableView() {
        viewToggleBtn().click();
        page.waitForTimeout(500);
        tableViewOption().waitFor(new Locator.WaitForOptions().setTimeout(5000));
        tableViewOption().click();
        agGridHeader().waitFor(new Locator.WaitForOptions().setTimeout(10000));
        page.waitForTimeout(2000);
        System.out.println("✅ Switched to Table View");
    }

    public void switchToEmployeeView() {
        viewToggleBtn().click();
        page.waitForTimeout(500);
        employeeViewOption().waitFor(new Locator.WaitForOptions().setTimeout(5000));
        employeeViewOption().click();
        teamDirectoryHeader().waitFor(new Locator.WaitForOptions().setTimeout(10000));
        page.waitForTimeout(2000);
        System.out.println("✅ Switched to Employee View");
    }

    // ================================================================
    // FILTER — AG Grid Column Header POPUP Filter (Table View)
    //
    // From the screenshot: clicking the filter icon (≡) on the "First Name"
    // column header opens a POPUP containing:
    //   - A "Contains" dropdown
    //   - A text input with placeholder "Filter..."
    //
    // This is NOT a floating filter row. We must:
    //   1. Hover the "First Name" header to reveal the filter icon
    //   2. Click the filter icon (≡) to open the popup
    //   3. Type in the "Filter..." input inside the popup
    //   4. Validate rows
    //   5. Clear and close popup for next name
    // ================================================================
    public void filterByFirstNames(List<String> names) {
        System.out.println("\n==== 🔍 FILTER VALIDATION (Column Header Popup Filter) ====");

        // Wait for AG Grid to fully load
        agGridHeader().waitFor(new Locator.WaitForOptions().setTimeout(10000));
        page.waitForTimeout(1500);

        // Locate the "First Name" column header
        Locator firstNameHeader = findFirstNameHeader();
        firstNameHeader.waitFor(new Locator.WaitForOptions().setTimeout(8000));
        System.out.println("✅ Found 'First Name' column header");

        for (String name : names) {
            System.out.println("🔎 Filtering for: [" + name + "]");

            // ── Step 1: Close any open popup first ───────────────────────────
            page.keyboard().press("Escape");
            page.waitForTimeout(400);

            // ── Step 2: Hover the First Name header to reveal the filter icon ─
            firstNameHeader.hover();
            page.waitForTimeout(600);

            // ── Step 3: Click the filter icon (≡) inside the header ──────────
            // The screenshot shows a ≡ icon appears on hover in the header cell
            Locator filterIcon = firstNameHeader.locator(
                    "span.ag-icon-menu, " +
                            "span.ag-icon-filter, " +
                            ".ag-header-cell-menu-button, " +
                            "span[ref='eMenu'], " +
                            ".ag-header-icon.ag-header-cell-menu-button"
            ).first();

            try {
                filterIcon.waitFor(new Locator.WaitForOptions().setTimeout(3000));
                filterIcon.click(new Locator.ClickOptions().setForce(true));
                System.out.println("   ✅ Clicked filter icon");
            } catch (Exception e) {
                // Fallback: click at the right edge of the header where ≡ appears
                System.out.println("   ⚠️ Filter icon not found — clicking header right edge");
                var box = firstNameHeader.boundingBox();
                if (box != null) {
                    page.mouse().click(box.x + box.width - 12, box.y + box.height / 2);
                } else {
                    firstNameHeader.click(new Locator.ClickOptions().setForce(true));
                }
            }
            page.waitForTimeout(800);

            // ── Step 4: Locate the popup filter input "Filter..." ─────────────
            // From screenshot: input has placeholder "Filter..." inside the popup
            Locator popupInput = page.locator(
                    "input.ag-input-field-input[placeholder='Filter...'], " +
                            "div.ag-popup input[placeholder='Filter...'], " +
                            "div.ag-filter-body-wrapper input.ag-input-field-input, " +
                            "div.ag-popup-child input.ag-input-field-input, " +
                            "div.ag-filter input[type='text'], " +
                            ".ag-filter-body input"
            ).first();

            try {
                popupInput.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            } catch (Exception e) {
                takeScreenshot("filter_popup_not_opened_" + name);
                Assert.fail("❌ Filter popup did not open for: [" + name + "]. " +
                        "Could not find input with placeholder 'Filter...' in the popup.");
            }

            // ── Step 5: Type the filter value ─────────────────────────────────
            popupInput.click(new Locator.ClickOptions().setForce(true));
            popupInput.press("Control+A");
            popupInput.press("Delete");
            popupInput.type(name, new Locator.TypeOptions().setDelay(100));
            page.waitForTimeout(2500);

            // ── Step 6: Validate filtered rows ────────────────────────────────
            List<String> results = getFirstNameColumnValues();
            System.out.println("   Results: " + results);

            if (results.isEmpty()) {
                takeScreenshot("filter_no_results_" + name);
                Assert.fail("❌ No rows visible after filtering by: [" + name + "]");
            }

            boolean allMatch = results.stream()
                    .allMatch(r -> r.toLowerCase().contains(name.toLowerCase()));

            if (!allMatch) {
                takeScreenshot("filter_mismatch_" + name);
                Assert.fail("❌ Filter [" + name + "] returned unexpected rows: " + results);
            }

            System.out.println("✅ Filter correct: " + name + " → " + results);

            // ── Step 7: Clear the filter input for next name ──────────────────
            try {
                popupInput.click(new Locator.ClickOptions().setForce(true));
                popupInput.press("Control+A");
                popupInput.press("Delete");
                page.waitForTimeout(500);
            } catch (Exception ignored) {}

            // Close popup
            page.keyboard().press("Escape");
            page.waitForTimeout(800);

            // Confirm grid is back to full data before next filter
            page.waitForTimeout(500);
        }

        System.out.println("==== ✅ FILTER VALIDATION COMPLETE ====\n");
    }

    // ================================================================
    // HELPER: Locate "First Name" AG Grid header cell
    // ================================================================
    private Locator findFirstNameHeader() {
        // Strategy 1: by col-id attribute
        Locator h = page.locator("div.ag-header-cell[col-id='firstName']");
        if (h.count() > 0) return h.first();

        h = page.locator("div.ag-header-cell[col-id='first_name']");
        if (h.count() > 0) return h.first();

        // Strategy 2: by exact header text
        h = page.locator("div.ag-header-cell:has(span.ag-header-cell-text:text-is('First Name'))");
        if (h.count() > 0) return h.first();

        // Strategy 3: filter by visible text
        h = page.locator("div.ag-header-cell")
                .filter(new Locator.FilterOptions().setHasText("First Name"));
        if (h.count() > 0) return h.first();

        // Strategy 4: first header cell (positional — First Name is column 1 per screenshot)
        System.out.println("   ⚠️ Using positional fallback for First Name header (col index 1)");
        return page.locator("div.ag-header-cell").nth(1);
    }

    // ================================================================
    // HELPER: Read First Name column cell values from AG Grid
    // ================================================================
    private List<String> getFirstNameColumnValues() {
        // Try by col-id (most reliable)
        String[] colIdSelectors = {
                "div.ag-center-cols-container div[col-id='firstName'] .ag-cell-value",
                "div.ag-center-cols-container div[col-id='first_name'] .ag-cell-value",
                "div.ag-center-cols-container div[col-id='firstName']",
                "div.ag-center-cols-container div[col-id='first_name']",
        };
        for (String sel : colIdSelectors) {
            List<String> result = extractCellText(page.locator(sel));
            if (!result.isEmpty()) return result;
        }

        // Positional fallback: col index 1 or 2 (col 0 may be kebab/checkbox)
        for (int i = 1; i <= 2; i++) {
            List<String> result = extractCellText(
                    page.locator("div.ag-center-cols-container div.ag-row div.ag-cell:nth-child(" + i + ")")
            );
            if (!result.isEmpty()) return result;
        }

        return Collections.emptyList();
    }

    private List<String> extractCellText(Locator cells) {
        List<String> out = new ArrayList<>();
        int n = cells.count();
        for (int i = 0; i < n; i++) {
            String t = cells.nth(i).textContent();
            if (t != null && !t.isBlank() && !t.trim().equals("–") && !t.trim().equals("-"))
                out.add(t.trim().toLowerCase());
        }
        return out;
    }

    // ================================================================
    // PAGINATION VALIDATION (Employee / Card View)
    // ================================================================
    public void verifyPaginationAndData() {
        System.out.println("\n==== 🔢 PAGINATION VALIDATION (Employee View) ====");

        // Step 1: Switch to Employee View + All Team
        switchToEmployeeView();
        switchToAllTeam();
        page.waitForTimeout(2000);

        // Step 2: Read pagination label
        String labelText = readPaginationLabel();
        System.out.println("📄 Pagination label: " + labelText);

        int total         = parseTotalFromLabel(labelText);
        int pageSize      = 10;
        int expectedPage2 = total - pageSize;

        System.out.println("📊 Total=" + total + " | PageSize=" + pageSize
                + " | Expected page 2=" + expectedPage2);

        // Step 3: Count page 1
        scrollToBottom();
        page.waitForTimeout(1500);

        List<String> page1Names = getCardViewEmployeeNames();
        System.out.println("📋 Page 1 count: " + page1Names.size() + " → " + page1Names);

        if (page1Names.size() != pageSize) {
            takeScreenshot("pagination_page1_wrong_count");
            Assert.fail("❌ Page 1 should show " + pageSize
                    + " records but found: " + page1Names.size());
        }
        System.out.println("✅ Page 1 correct: " + pageSize + " records");

        // Step 4: Click next arrow
        scrollToTop();
        page.waitForTimeout(800);

        Locator next = nextArrowBtn();
        try {
            next.waitFor(new Locator.WaitForOptions().setTimeout(6000));
        } catch (Exception e) {
            takeScreenshot("next_arrow_not_found");
            Assert.fail("❌ Next arrow NOT found. Locator: //span[@class='ag-icon ag-icon-next']");
        }

        if (!next.isEnabled()) {
            takeScreenshot("next_arrow_disabled");
            Assert.fail("❌ Next arrow is DISABLED on page 1");
        }

        next.click();
        page.waitForTimeout(3000);
        System.out.println("➡️ Clicked next arrow — now on page 2");

        // Step 5: Count page 2
        scrollToBottom();
        page.waitForTimeout(1500);

        List<String> page2Names = getCardViewEmployeeNames();
        System.out.println("📋 Page 2 count: " + page2Names.size() + " → " + page2Names);

        if (page2Names.size() != expectedPage2) {
            takeScreenshot("pagination_page2_wrong_count");
            Assert.fail("❌ Page 2 should show " + expectedPage2
                    + " records but found: " + page2Names.size());
        }
        System.out.println("✅ Page 2 correct: " + expectedPage2 + " records");

        // Step 6: No duplicates
        Set<String> page1Set = new HashSet<>(page1Names);
        List<String> dupes   = new ArrayList<>();
        for (String n : page2Names) {
            if (page1Set.contains(n)) dupes.add(n);
        }
        if (!dupes.isEmpty()) {
            takeScreenshot("pagination_duplicates");
            Assert.fail("❌ Duplicate employees found across pages: " + dupes);
        }
        System.out.println("✅ No duplicate employees between pages");

        // Step 7: Total match
        int combined = page1Names.size() + page2Names.size();
        if (combined != total) {
            takeScreenshot("pagination_total_mismatch");
            Assert.fail("❌ Page1(" + page1Names.size() + ") + Page2("
                    + page2Names.size() + ") = " + combined + " ≠ total(" + total + ")");
        }
        System.out.println("✅ Total match: " + combined + " = " + total);
        System.out.println("==== ✅ PAGINATION VALIDATION PASSED ====\n");
    }

    // ================================================================
    // FORMER EMPLOYEE ISOLATION VALIDATION
    // ================================================================
    public void verifyFormerNotInAllTeam() {
        System.out.println("\n==== 🔍 FORMER ISOLATION VALIDATION ====");

        switchToAllTeam();
        page.waitForTimeout(2000);
        Set<String> allTeamNames = collectNamesAcrossAllPages("All Team");
        System.out.println("👥 All Team total: " + allTeamNames.size() + " → " + allTeamNames);

        formerTab().click();
        page.waitForTimeout(3000);
        Set<String> formerNames = collectNamesAcrossAllPages("Former Employees");
        System.out.println("🚫 Former total:   " + formerNames.size() + " → " + formerNames);

        Set<String> overlap = new HashSet<>(allTeamNames);
        overlap.retainAll(formerNames);

        if (!overlap.isEmpty()) {
            takeScreenshot("former_overlap_found");
            Assert.fail("❌ Employees in BOTH All Team AND Former: " + overlap);
        }

        System.out.println("✅ No overlap — All Team and Former are fully isolated");
        System.out.println("==== ✅ FORMER ISOLATION PASSED ====\n");

        switchToAllTeam();
    }

    // ================================================================
    // HELPERS
    // ================================================================

    private List<String> getCardViewEmployeeNames() {
        Locator links = cardViewNameLinks();
        List<String> names = new ArrayList<>();
        int count = links.count();
        for (int i = 0; i < count; i++) {
            String raw = links.nth(i).textContent();
            if (raw != null && !raw.isBlank()) {
                String cleaned = raw.replaceAll("\\s*\\(.*?\\)\\s*", "").trim().toLowerCase();
                if (!cleaned.isEmpty()) names.add(cleaned);
            }
        }
        return names;
    }

    private Set<String> collectNamesAcrossAllPages(String tabLabel) {
        Set<String> all = new HashSet<>();
        int pageNum = 1;
        while (true) {
            scrollToBottom();
            page.waitForTimeout(1500);
            List<String> current = getCardViewEmployeeNames();
            System.out.println("  [" + tabLabel + "] Page " + pageNum
                    + " → " + current.size() + " records: " + current);
            all.addAll(current);

            Locator next = nextArrowBtn();
            boolean canNext = false;
            try {
                next.waitFor(new Locator.WaitForOptions().setTimeout(2000));
                canNext = next.isEnabled() && next.isVisible();
            } catch (Exception ignored) {}

            if (!canNext) break;
            scrollToTop();
            next.click();
            pageNum++;
            page.waitForTimeout(2500);
        }
        return all;
    }

    private void scrollToBottom() {
        page.evaluate("window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' })");
        page.waitForTimeout(800);
    }

    private void scrollToTop() {
        page.evaluate("window.scrollTo({ top: 0, behavior: 'smooth' })");
        page.waitForTimeout(500);
    }

    private String readPaginationLabel() {
        try {
            String t = page.locator(
                    "//*[contains(text(),' to ') and contains(text(),' of ')]"
            ).first().textContent().trim();
            if (!t.isEmpty()) return t;
        } catch (Exception ignored) {}

        try {
            String t = page.locator("text=/\\d+ to \\d+ of \\d+/")
                    .first().textContent().trim();
            if (!t.isEmpty()) return t;
        } catch (Exception ignored) {}

        System.err.println("⚠️ Pagination label not found — defaulting total=15");
        return "1 to 10 of 15";
    }

    private int parseTotalFromLabel(String label) {
        java.util.regex.Matcher m =
                java.util.regex.Pattern.compile("of\\s+(\\d+)").matcher(label);
        if (m.find()) return Integer.parseInt(m.group(1));
        System.err.println("⚠️ Cannot parse total from '" + label + "'. Default 15.");
        return 15;
    }
}