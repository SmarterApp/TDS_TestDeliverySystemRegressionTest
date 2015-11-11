package tests.student.general;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the help widget
 *
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
    public void testDragDialog() throws InterruptedException {
        driver.findElement(By.cssSelector("a#btnHelp")).click();
        driver.waitForAndGetElementByLocator(By.className(HELP_DIALOG_CLASS));
        WebElement header = driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS+ " h2.hd"));
                Point headerLocation = header.getLocation();
        driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS+ " h2.hd"));

        Point moveTo = new Point(200, -100);
        // This wait is necessary for the UI to update and for test to succeed in non-debug mode
        Thread.sleep(500);
        Actions builder = new Actions(driver);
        builder.moveToElement(header, 10, 10)
                .clickAndHold()
                .moveByOffset(moveTo.getX(), moveTo.getY())
                .release()
                .build().perform();

        //Ensure that the dialog was moved
        assertEquals(header.getLocation(),
                new Point(headerLocation.getX() + moveTo.getX(), headerLocation.getY() + moveTo.getY()));
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
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector(
                STREAMLINED_SELECT_CSS_SELECTOR + " option[value='" + STREAMLINED_ON_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID")).getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Wait for test to load
        driver.waitForTitle("Student: Test", true);

        //Click help button
        driver.findElement(By.cssSelector("a#btnHelp")).click();
        driver.waitForAndGetElementByLocator(By.className(HELP_DIALOG_CLASS));
        assertTrue(driver.isElementVisibleNow(By.className(HELP_DIALOG_CLASS)));
        WebElement helpContainer = driver.findElement(By.className(HELP_DIALOG_CLASS));
        assertTrue(helpContainer.findElement(By.cssSelector("h2.hd")).getText().contains("Help Guide"));

        //Switch into "help" iframe
        WebElement helpFrame = driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS + " iframe"));
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
