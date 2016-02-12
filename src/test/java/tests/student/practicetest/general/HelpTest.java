package tests.student.practicetest.general;

import com.googlecode.zohhak.api.TestWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import driver.BrowserInteractionType;
import tests.StudentPracticeTestBaseTest;
import tests.categories.StudentGuestTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the help widget
 *
 * Created by emunoz on 10/27/15.
 */
public class HelpTest extends StudentPracticeTestBaseTest {
    private static final String STREAMLINED_ON_OPTION = "TDS_SLM1";
    private static final String STREAMLINED_SELECT_CSS_SELECTOR = "select[id*='streamlinedmode']";
    private static final String HELP_DIALOG_CLASS = "tool-helpguide-container";

    @Before
    public void openHomeAndLogin() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();
    }

    @Test
    @Category(StudentGuestTest.class)
    public void testDragDialog() throws InterruptedException {
        Thread.sleep(2000);
        driver.waitForAndFindElement(By.cssSelector("a#btnHelp")).click();
        driver.waitForAndFindElement(By.className(HELP_DIALOG_CLASS));
        WebElement header = driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS+ " h2.hd"));
                Point headerLocation = header.getLocation();
        driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS+ " h2.hd"));

        Point moveTo = new Point(200, -100);
        // This wait is necessary for the UI to update and for test to succeed in non-debug mode
        Thread.sleep(2000);
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

    @Category(StudentGuestTest.class)
    //@TestWith({ "KEYBOARD", "MOUSE" })
    @TestWith({"MOUSE"})
    public void testHelp(BrowserInteractionType interactionType) throws Exception {
        Thread.sleep(3000);
        if (interactionType == BrowserInteractionType.KEYBOARD) {
            driver.findElement(By.cssSelector("a#btnHelp"), interactionType).sendKeys(Keys.ENTER);
        } else {
            driver.findElement(By.cssSelector("a#btnHelp"), interactionType).click();
        }
        driver.waitForAndFindElement(By.className(HELP_DIALOG_CLASS));
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
        if (interactionType == BrowserInteractionType.KEYBOARD) {
            driver.pressKey(Keys.ESCAPE);
        } else {
            driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS + " a.container-close")).click();
        }

        assertFalse(driver.isElementVisibleNow(By.className(HELP_DIALOG_CLASS)));
    }

    @Test
    public void     testHelpStreamlined() {
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
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Wait for test to load
        driver.waitForTitle("Student: Test", true);

        //Click help button
        driver.findElement(By.cssSelector("a#btnHelp")).click();
        driver.waitForAndFindElement(By.className(HELP_DIALOG_CLASS));
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
