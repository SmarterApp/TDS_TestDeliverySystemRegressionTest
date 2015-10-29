package tests.student.general;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 10/27/15.
 */
public class HelpTest extends SeleniumBaseTest {
    private static final String STREAMLINED_ON_OPTION = "TDS_SLM1";

    private static final String STREAMLINED_SELECT_CSS_SELECTOR = "select[id*='streamlinedmode']";

    private static final String HELP_DIALOG_CLASS = "tool-helpguide-container";

    @Before
    public void openHomeAndLogin() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();
    }

    @Test
    public void testHelp() {
        driver.findElement(By.cssSelector("a#btnHelp")).click();
        driver.waitForAndGetElementByLocator(By.className(HELP_DIALOG_CLASS));
        assertTrue(driver.isElementVisibleNow(By.className(HELP_DIALOG_CLASS)));
        WebElement helpContainer = driver.findElement(By.className(HELP_DIALOG_CLASS));
        assertTrue(helpContainer.findElement(By.cssSelector("h2.hd")).getText().contains("Help Guide"));

        //Switch into "help" iframe
        WebElement helpFrame = driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS+ " iframe"));
        driver.switchTo().frame(helpFrame);

        //Ensure HELP links are available and visible
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#one']")).getText(),
                "Overview of the Student Testing Site");
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#two']")).getText(),
                "Test Rules");
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#three']")).getText(),
                "About Print-on-Demand and Text-to-Speech");

        // Switch back to main document context
        driver.switchTo().defaultContent();

        // Close help dialog
        driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS + " a.container-close")).click();
                assertFalse(driver.isElementVisibleNow(By.className(HELP_DIALOG_CLASS)));
    }

    @Test
    public void testHelpStreamlined() {
        //Grade 3
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector(
                STREAMLINED_SELECT_CSS_SELECTOR + " option[value='" + STREAMLINED_ON_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Wait for test to load
        driver.waitForTitleAndAssert("Student: Test", true);

        //Click help button
        driver.findElement(By.cssSelector("a#btnHelp")).click();
        driver.waitForAndGetElementByLocator(By.className(HELP_DIALOG_CLASS));
        assertTrue(driver.isElementVisibleNow(By.className(HELP_DIALOG_CLASS)));
        WebElement helpContainer = driver.findElement(By.className(HELP_DIALOG_CLASS));
        assertTrue(helpContainer.findElement(By.cssSelector("h2.hd")).getText().contains("Help Guide"));

        //Switch into "help" iframe
        WebElement helpFrame = driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS+ " iframe"));
        driver.switchTo().frame(helpFrame);

        //Ensure HELP links are available and visible
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#two']")).getText(),
                "Basic Test Rules");
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#three']")).getText(),
                "Overview of the Test Tools");
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#four']")).getText(),
                "Navigating the Test with Job Access With Speech (JAWS)");
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#five']")).getText(),
                "Responding to Items with JAWS");
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#six']")).getText(),
                "Navigating the Test without JAWS");
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#seven']")).getText(),
                "Responding to Items without JAWS");

        // Switch back to main document context
        driver.switchTo().defaultContent();

        // Close help dialog
        driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS + " a.container-close")).click();
        assertFalse(driver.isElementVisibleNow(By.className(HELP_DIALOG_CLASS)));
    }
}
