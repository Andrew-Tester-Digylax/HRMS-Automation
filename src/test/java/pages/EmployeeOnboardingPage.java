
package pages;

import com.microsoft.playwright.*;
        import com.microsoft.playwright.options.LoadState;
import org.testng.Assert;

import java.io.File;
import java.nio.file.Paths;

public class EmployeeOnboardingPage {

    private final Page page;

    public EmployeeOnboardingPage(Page page) {
        this.page = page;
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

    private void wait(int ms) {
        page.waitForTimeout(ms);
    }

    // ================================================================
    // STEP 1 — Click Employee Module
    // ================================================================
    public void clickEmployeeModule() {
        System.out.println("➡️ Clicking Employee Module...");
        Locator moduleIcon = page.locator("//*[name()='path' and contains(@d,'M53.4254 2')]");
        try {
            moduleIcon.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            moduleIcon.click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            System.out.println("   ⚠️ SVG path locator failed — using text fallback");
            page.locator("div.module-name:has-text('Employees')").first()
                    .click(new Locator.ClickOptions().setForce(true));
        }
        page.waitForLoadState(LoadState.NETWORKIDLE,
                new Page.WaitForLoadStateOptions().setTimeout(15000));
        wait(1000);
        System.out.println("✅ Employee Module clicked");
    }

    // ================================================================
    // STEP 2 — Click "Onboard Employee" Button
    // ================================================================
    public void clickOnboardEmployee() {
        System.out.println("➡️ Clicking Onboard Employee button...");
        Locator btn = page.locator("//span[@class='ps-2']");
        try {
            btn.waitFor(new Locator.WaitForOptions().setTimeout(40000));
            btn.click();
        } catch (Exception e) {
            page.locator("button:has-text('Onboard Employee')").first().click();
        }
        page.waitForLoadState(LoadState.NETWORKIDLE,
                new Page.WaitForLoadStateOptions().setTimeout(15000));
        wait(1000);
        System.out.println("✅ Onboard Employee clicked — on Step 1 form");
    }

    // ================================================================
    // STEP 3 — Fill Basic Details
    // ================================================================

    public void enterFirstName(String firstName) {
        System.out.println("➡️ Entering First Name: " + firstName);
        Locator field = page.locator("//input[@placeholder='First Name']");
        field.waitFor(new Locator.WaitForOptions().setTimeout(8000));
        field.click();
        field.fill(firstName);
        wait(1000);
        System.out.println("✅ First Name entered: " + firstName);
    }

    public void enterLastName(String lastName) {
        System.out.println("➡️ Entering Last Name: " + lastName);
        Locator field = page.locator("//input[@placeholder='Last Name']");
        field.waitFor(new Locator.WaitForOptions().setTimeout(8000));
        field.click();
        field.fill(lastName);
        wait(1000);
        System.out.println("✅ Last Name entered: " + lastName);
    }

    public void enterOfficeEmail(String email) {
        System.out.println("➡️ Entering Office Email: " + email);
        Locator field = page.locator("//input[@placeholder='Office Email']");
        field.waitFor(new Locator.WaitForOptions().setTimeout(8000));
        field.click();
        field.fill(email);
        wait(1000);
        System.out.println("✅ Office Email entered: " + email);
    }

    public void selectGender() {
        System.out.println("➡️ Selecting Gender...");
        Locator dropdown = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[1]/div[1]/div[1]" +
                        "/div[2]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/*[name()='svg'][1]"
        );
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator options = page.locator("div[class*='select__option']");
            if (options.count() > 0) {
                options.first().click();
                System.out.println("✅ Gender selected: " + options.first().textContent().trim());
            } else {
                System.out.println("⚠️ No gender options visible — skipping");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Gender dropdown not found: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectDateOfJoining() {
        System.out.println("➡️ Selecting Date of Joining...");
        Locator calBtn = page.locator(
                "//div[@class='row']//div[@class='col-md-4 left-side']//div[@class='mb-4']" +
                        "//div//div[@class='form-label-group in-border mb-0']//*[name()='svg']"
        );
        try {
            calBtn.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            calBtn.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator targetDay = page.locator("//div[@aria-label='Choose Friday, April 3rd, 2026']");
            try {
                targetDay.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                targetDay.click();
                System.out.println("✅ Date of Joining selected: April 3, 2026");
            } catch (Exception e) {
                Locator anyDay = page.locator(
                        "div[class*='react-datepicker__day']:not([class*='disabled'])").first();
                if (anyDay.count() > 0) {
                    anyDay.click();
                    System.out.println("✅ Date of Joining: fallback date selected");
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Date of Joining calendar not found: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectDateOfBirth() {
        System.out.println("➡️ Selecting Date of Birth...");
        Locator calBtn = page.locator(
                "//div[@class='row']//div[@class='col-md-4 right-side']//div[@class='mb-4']" +
                        "//div//div[@class='form-label-group in-border mb-0']//*[name()='svg']"
        );
        try {
            calBtn.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            calBtn.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator anyDay = page.locator(
                    "div[class*='react-datepicker__day']:not([class*='disabled'])").first();
            if (anyDay.count() > 0) {
                anyDay.click();
                System.out.println("✅ Date of Birth selected");
            } else {
                System.out.println("⚠️ No available DOB dates — skipping");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Date of Birth calendar not found: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectMaritalStatus() {
        System.out.println("➡️ Selecting Marital Status...");
        Locator dropdown = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[1]/div[1]/div[1]" +
                        "/div[2]/div[3]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/*[name()='svg'][1]"
        );
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator single = page.locator("//div[contains(@class,'select__menu')]//div[text()='Single']");
            single.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            single.click();
            System.out.println("✅ Marital Status: Single selected");
        } catch (Exception e) {
            System.out.println("⚠️ Marital Status dropdown failed: " + e.getMessage());
        }
        wait(1000);
    }

    // ================================================================
    // STEP 3B — Employee Details Section
    // ================================================================

    public void enterEmployeeId(String empId) {
        System.out.println("➡️ Entering Employee ID: " + empId);
        Locator field = page.locator("//input[@placeholder='Employee ID']");
        field.waitFor(new Locator.WaitForOptions().setTimeout(8000));
        field.click();
        field.fill(empId);
        wait(1000);
        System.out.println("✅ Employee ID entered: " + empId);
    }

    public void selectDesignation() {
        System.out.println("➡️ Selecting Designation...");
        Locator dropdown = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[1]/div[1]/div[1]" +
                        "/div[4]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/*[name()='svg'][1]"
        );
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator seniorSWE = page.locator(
                    "div[class*='select__option']:has-text('Senior Software Engineer')").first();
            if (seniorSWE.count() > 0) {
                seniorSWE.click();
                System.out.println("✅ Designation: Senior Software Engineer selected");
            } else {
                Locator first = page.locator("div[class*='select__option']").first();
                if (first.count() > 0) {
                    String text = first.textContent().trim();
                    first.click();
                    System.out.println("✅ Designation fallback selected: " + text);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Designation dropdown failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectRole() {
        System.out.println("➡️ Selecting Role...");
        Locator dropdown = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[1]/div[1]/div[1]" +
                        "/div[4]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]"
        );
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator employeeOption = page.locator(
                    "div[class*='select__option']:has-text('Employee')").first();
            if (employeeOption.count() > 0) {
                employeeOption.click();
                System.out.println("✅ Role: Employee selected");
            } else {
                Locator first = page.locator("div[class*='select__option']").first();
                if (first.count() > 0) {
                    String text = first.textContent().trim();
                    first.click();
                    System.out.println("✅ Role fallback selected: " + text);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Role dropdown failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectPosition() {
        System.out.println("➡️ Selecting Position...");
        Locator dropdown = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[1]/div[1]/div[1]" +
                        "/div[4]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/*[name()='svg'][1]"
        );
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator options = page.locator("div[class*='select__option']");
            if (options.count() > 0) {
                String text = options.first().textContent().trim();
                options.first().click();
                System.out.println("✅ Position selected: " + text);
            } else {
                System.out.println("⚠️ No positions available — skipping");
                page.keyboard().press("Escape");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Position dropdown failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectDepartment() {
        System.out.println("➡️ Selecting Department...");
        Locator dropdown = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[1]/div[1]/div[1]" +
                        "/div[4]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/*[name()='svg'][1]"
        );
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator qa = page.locator(
                    "div[class*='select__option']:has-text('QA')").first();
            if (qa.count() > 0) {
                String text = qa.textContent().trim();
                qa.click();
                System.out.println("✅ Department selected: " + text);
            } else {
                Locator first = page.locator("div[class*='select__option']").first();
                if (first.count() > 0) {
                    String text = first.textContent().trim();
                    first.click();
                    System.out.println("✅ Department fallback selected: " + text);
                } else {
                    System.out.println("⚠️ No departments visible — skipping");
                    page.keyboard().press("Escape");
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Department dropdown failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectContributionLevel() {
        System.out.println("➡️ Selecting Contribution Level...");
        Locator dropdown = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[1]/div[1]/div[1]" +
                        "/div[4]/div[3]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/*[name()='svg'][1]"
        );
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator team = page.locator(
                    "div[class*='select__option']:has-text('Team')").first();
            if (team.count() > 0) {
                String text = team.textContent().trim();
                team.click();
                System.out.println("✅ Contribution Level selected: " + text);
            } else {
                Locator first = page.locator("div[class*='select__option']").first();
                if (first.count() > 0) {
                    String text = first.textContent().trim();
                    first.click();
                    System.out.println("✅ Contribution Level fallback selected: " + text);
                } else {
                    System.out.println("⚠️ No contribution levels visible — skipping");
                    page.keyboard().press("Escape");
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Contribution Level dropdown failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectEmployeeType() {
        System.out.println("➡️ Selecting Employee Type...");
        Locator dropdown = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[1]/div[1]/div[1]" +
                        "/div[4]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/*[name()='svg'][1]"
        );
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator fullTime = page.locator(
                    "div[class*='select__option']:has-text('Full Time')").first();
            if (fullTime.count() > 0) {
                fullTime.click();
                System.out.println("✅ Employee Type: Full Time selected");
            } else {
                Locator first = page.locator("div[class*='select__option']").first();
                if (first.count() > 0) {
                    String text = first.textContent().trim();
                    first.click();
                    System.out.println("✅ Employee Type fallback selected: " + text);
                } else {
                    System.out.println("⚠️ No employee types visible — skipping");
                    page.keyboard().press("Escape");
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Employee Type dropdown failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectBaseOfficeLocation() {
        System.out.println("➡️ Selecting Base Office Location...");
        Locator dropdown = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[1]/div[1]/div[1]" +
                        "/div[4]/div[1]/div[4]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]"
        );
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator options = page.locator("div[class*='select__option']");
            if (options.count() > 0) {
                String text = options.first().textContent().trim();
                options.first().click();
                System.out.println("✅ Base Office Location selected: " + text);
            } else {
                System.out.println("⚠️ No base office locations visible — skipping");
                page.keyboard().press("Escape");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Base Office Location dropdown failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectCurrentOfficeLocation() {
        System.out.println("➡️ Selecting Current Office Location...");
        Locator dropdown = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[1]/div[1]/div[1]" +
                        "/div[4]/div[2]/div[4]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/*[name()='svg'][1]"
        );
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator options = page.locator("div[class*='select__option']");
            if (options.count() > 0) {
                String text = options.first().textContent().trim();
                options.first().click();
                System.out.println("✅ Current Office Location selected: " + text);
            } else {
                System.out.println("⚠️ No current office locations visible — skipping");
                page.keyboard().press("Escape");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Current Office Location dropdown failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectReportingManager() {
        System.out.println("➡️ Selecting Reporting Manager...");
        Locator dropdown = page.locator(
                "div:has-text('Choose Reporting Manager') svg").last();
        try {
            dropdown.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            dropdown.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator options = page.locator("div[class*='select__option']");
            if (options.count() > 0) {
                String text = options.first().textContent().trim();
                options.first().click();
                System.out.println("✅ Reporting Manager selected: " + text);
            } else {
                System.out.println("⚠️ No reporting managers visible — skipping");
                page.keyboard().press("Escape");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Reporting Manager dropdown failed: " + e.getMessage());
        }
        wait(3000);
    }

    // ================================================================
    // STEP 4 — Click "Save & Next" (Step 1 → Step 2)
    // ================================================================
    public void clickSaveAndNext() {
        System.out.println("➡️ Clicking Save & Next...");
        Locator btn = page.locator("//button[normalize-space()='Save & Next']");
        try {
            btn.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            if (!btn.isEnabled()) {
                takeScreenshot("save_and_next_disabled");
                Assert.fail("❌ Save & Next button is disabled — required fields may be missing");
            }
            btn.click();
        } catch (Exception e) {
            takeScreenshot("save_and_next_error");
            Assert.fail("❌ Save & Next button not found or not clickable: " + e.getMessage());
        }
        page.waitForLoadState(LoadState.NETWORKIDLE,
                new Page.WaitForLoadStateOptions().setTimeout(15000));
        wait(2000);
        System.out.println("✅ Save & Next clicked — navigated to Step 2");
    }

    // ================================================================
    // STEP 5 — Educational Details (Step 2)
    // ================================================================

    public void selectDegree() {
        System.out.println("➡️ Selecting Degree...");
        Locator select = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[2]/div[1]/div[1]" +
                        "/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/select[1]"
        );
        try {
            select.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            int optCount = select.locator("option").count();
            if (optCount > 1) {
                String value = select.locator("option").nth(1).getAttribute("value");
                select.selectOption(value);
                System.out.println("✅ Degree selected");
            } else {
                System.out.println("⚠️ No degree options available — skipping");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Degree field failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectCourse() {
        System.out.println("➡️ Selecting Course...");
        Locator select = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[2]/div[1]/div[1]" +
                        "/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/select[1]"
        );
        try {
            select.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            int optCount = select.locator("option").count();
            if (optCount > 1) {
                String value = select.locator("option").nth(1).getAttribute("value");
                select.selectOption(value);
                System.out.println("✅ Course selected");
            } else {
                System.out.println("⚠️ No course options available — skipping");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Course field failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectEducationStartDate() {
        System.out.println("➡️ Selecting Education Start Date...");
        Locator calBtn = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[2]/div[1]/div[1]" +
                        "/div[2]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/*[name()='svg'][1]"
        );
        try {
            calBtn.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            calBtn.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator anyDay = page.locator(
                    "div[class*='react-datepicker__day']:not([class*='disabled'])").first();
            if (anyDay.count() > 0) {
                anyDay.click();
                System.out.println("✅ Education Start Date selected");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Education Start Date calendar failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectEducationEndDate() {
        System.out.println("➡️ Selecting Education End Date...");
        Locator calBtn = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[2]/div[1]/div[1]" +
                        "/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/*[name()='svg'][1]"
        );
        try {
            calBtn.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            calBtn.click(new Locator.ClickOptions().setForce(true));
            wait(1000);

            Locator anyDay = page.locator(
                    "div[class*='react-datepicker__day']:not([class*='disabled'])").first();
            if (anyDay.count() > 0) {
                anyDay.click();
                System.out.println("✅ Education End Date selected");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Education End Date calendar failed: " + e.getMessage());
        }
        wait(1000);
    }

    public void selectCGPA() {
        System.out.println("➡️ Selecting CGPA...");
        Locator select = page.locator(
                "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/div[2]/div[1]/div[1]" +
                        "/div[2]/div[2]/div[2]/div[1]/div[1]/div[1]/select[1]"
        );
        try {
            select.waitFor(new Locator.WaitForOptions().setTimeout(8000));
            int optCount = select.locator("option").count();
            if (optCount > 1) {
                String value = select.locator("option").nth(1).getAttribute("value");
                select.selectOption(value);
                System.out.println("✅ CGPA selected");
            } else {
                System.out.println("⚠️ No CGPA options available — skipping");
            }
        } catch (Exception e) {
            System.out.println("⚠️ CGPA field failed: " + e.getMessage());
        }
        wait(1000);
    }

    // ================================================================
    // STEP 6 — Click "Save & Next" / "Save & Exit" on Step 2
    // ================================================================
    public void clickSaveAndExit() {
        System.out.println("➡️ Clicking Save & Next / Save & Exit on Step 2...");
        Locator btn = page.locator("//button[@type='submit']");
        try {
            btn.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            btn.click();
        } catch (Exception e) {
            takeScreenshot("save_exit_error");
            Assert.fail("❌ Submit button on Step 2 not found: " + e.getMessage());
        }
        page.waitForLoadState(LoadState.NETWORKIDLE,
                new Page.WaitForLoadStateOptions().setTimeout(15000));
        wait(2000);
        System.out.println("✅ Step 2 submitted — onboarding complete");
    }
}