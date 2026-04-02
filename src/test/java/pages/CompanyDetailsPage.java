package pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

public class CompanyDetailsPage {

    private Page page;

    public CompanyDetailsPage(Page page) {
        this.page = page;
    }

    // Locators
    private String companyName = "//input[@placeholder='Company Name']";
    private String companyPhone = "//input[@placeholder='Company Phone Number']";
    private String accessUrl = "//input[@id='accessUrl']";
    private String getStartedBtn = "//span[normalize-space()='Get Started']";

    // Wait for page load
    public void waitForPage() {
        page.waitForSelector(companyName);
    }

    // Common wait
    private void waitSeconds(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Fill details
    public void fillCompanyDetails(String name, String phone, String domain) {

        page.waitForSelector(companyName);

        // 1. Company Name
        page.locator(companyName).fill(name);
        waitSeconds(4);

        // 2. Phone Number
        page.locator(companyPhone).fill(phone);
        waitSeconds(4);

        // 3. Access URL (TYPE ONE BY ONE — CRITICAL FIX)
        Locator access = page.locator(accessUrl);

        access.click();

        // Clear field
        page.keyboard().press("Control+A");
        page.keyboard().press("Delete");

        // Type character by character
        for (char c : domain.toCharArray()) {
            page.keyboard().type(String.valueOf(c));
            waitSeconds(1); // delay between each letter
        }

        System.out.println("Typed domain: " + domain);

        // ⏱ WAIT 7 sec after typing
        waitSeconds(7);

        // Verify button enabled
        boolean enabled = page.locator(getStartedBtn).isEnabled();
        System.out.println("Get Started enabled: " + enabled);

        if (!enabled) {
            throw new RuntimeException("Get Started button still disabled after entering data");
        }
    }

    // Click button
    public void clickGetStarted() {

        page.waitForSelector(getStartedBtn);

        page.locator(getStartedBtn).scrollIntoViewIfNeeded();

        page.locator(getStartedBtn)
                .click(new Locator.ClickOptions().setForce(true));

        // ⏱ WAIT 10 sec after click
        waitSeconds(10);

        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    // Get value for validation
    public String getAccessUrl() {
        return page.inputValue(accessUrl);
    }
}