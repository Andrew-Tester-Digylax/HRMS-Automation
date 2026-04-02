package pages;

import com.microsoft.playwright.*;

public class LoginPage {

    private Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    // Locators (FIXED)
    private String emailField = "//input[@placeholder='Email, Phone Number, or Employee ID']";
    private String nextBtn = "//button[normalize-space()='Next']";

    private String passwordField = "//input[@placeholder='Password']";
    private String loginBtn = "//button[normalize-space()='Login']"; // ✅ FIXED

    // Step 1: Email → Next
    public void enterEmailAndClickNext(String email) {

        page.waitForSelector(emailField);

        page.fill(emailField, email);

        page.locator(nextBtn).click();

        // Wait for password screen
        page.waitForSelector(passwordField);
    }

    // Step 2: Password → Login
    public void enterPasswordAndLogin(String password) {

        page.fill(passwordField, password);

        // Wait until button is enabled (IMPORTANT)
        page.waitForSelector(loginBtn);

        boolean enabled = page.locator(loginBtn).isEnabled();
        System.out.println("Login button enabled: " + enabled);

        if (!enabled) {
            throw new RuntimeException("Login button is disabled");
        }

        page.locator(loginBtn).click();

        // ⏱ YOUR REQUIREMENT
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ✅ REAL WAIT (not useless one)
        page.waitForURL("**/dashboard");
    }
}