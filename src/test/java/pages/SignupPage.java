package pages;

import com.microsoft.playwright.*;

public class SignupPage {

    private Page page;

    public SignupPage(Page page) {
        this.page = page;
    }

    // Locators
    private String firstName = "//input[@placeholder='First Name']";
    private String lastName = "//input[@placeholder='Last Name']";
    private String email = "//input[@placeholder='Work Email']";
    private String phone = "//input[@placeholder='Phone Number']";
    private String password = "//input[@placeholder='Password']";
    private String confirmPassword = "//input[@placeholder='Confirm password']";
    private String terms = "//input[@id='agreeToc']";

    private String signupBtn = "//button[@id='kt_sign_in_submit' and not(@disabled)]";

    // ---------------- ACTIONS ----------------

    public void fillSignupForm(String fn, String ln, String mail, String pass) {

        page.waitForSelector(firstName);

        page.fill(firstName, fn);
        page.fill(lastName, ln);
        page.fill(email, mail);

        // PHONE
        page.waitForSelector(phone);
        page.locator(phone).click();
        page.locator(phone).fill("");
        page.locator(phone).type("9847143610");

        // PASSWORD
        page.waitForSelector(password);
        page.locator(password).click();
        page.locator(password).type(pass);

        page.locator(confirmPassword).click();
        page.locator(confirmPassword).type(pass);

        // CHECKBOX
        page.locator(terms).check(new Locator.CheckOptions().setForce(true));

        // ⏱ WAIT 5 seconds AFTER checkbox
        wait5Seconds();

        page.waitForSelector(signupBtn);

        System.out.println("Button enabled: " + page.isEnabled(signupBtn));
    }

    public void clickSignup() {

        page.waitForSelector(signupBtn);

        page.locator(signupBtn).click();

        // ⏱ WAIT 5 seconds AFTER click
        wait5Seconds();
    }

    // ---------------- COMMON WAIT ----------------

    private void wait5Seconds() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}