package base;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.nio.file.Paths;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();

        // ── Detect if running in CI (Docker / GitHub Actions) ─────────────
        // In CI there is no display, so we MUST use headless mode.
        // Locally (Windows/Mac) we run headed so you can watch the browser.
        boolean isCI = System.getenv("CI") != null
                || System.getenv("GITHUB_ACTIONS") != null
                || System.getProperty("headless", "false").equalsIgnoreCase("true");

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(isCI);   // headless=true in CI, false locally

        if (!isCI) {
            // Maximized window only makes sense in headed mode
            launchOptions.setArgs(java.util.List.of("--start-maximized"));
        }

        browser = playwright.chromium().launch(launchOptions);

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        if (!isCI) {
            contextOptions.setViewportSize(null); // use full screen size locally
        } else {
            contextOptions.setViewportSize(1920, 1080); // fixed size in CI
        }

        context = browser.newContext(contextOptions);
        page    = context.newPage();

        System.out.println("🚀 Browser launched in "
                + (isCI ? "HEADLESS (CI)" : "MAXIMIZED") + " mode");
    }

    @AfterMethod
    public void tearDown() {
        // Save a failure screenshot if test failed
        try {
            if (page != null) {
                File dir = new File("screenshots");
                if (!dir.exists()) dir.mkdirs();
                String path = "screenshots/verifyEmployeesFlow_FAILED_"
                        + System.currentTimeMillis() + ".png";
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get(path))
                        .setFullPage(true));
                System.out.println("📸 Failure screenshot saved: " + path);
            }
        } catch (Exception ignored) {}

        if (context    != null) context.close();
        if (browser    != null) browser.close();
        if (playwright != null) playwright.close();
        System.out.println("🔒 Browser closed");
    }
}