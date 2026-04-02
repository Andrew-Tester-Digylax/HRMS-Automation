package pages;

import com.microsoft.playwright.*;

public class OrganizationPage {

    private Page page;

    public OrganizationPage(Page page) {
        this.page = page;
    }

    public void selectITOrganization() {
        //page.locator("(//h5[text()='IT / Software'])[1]").click();
        page.locator("//button[@class='btn btn-primary mt-2 px-4 py-2']").click();
    }
}