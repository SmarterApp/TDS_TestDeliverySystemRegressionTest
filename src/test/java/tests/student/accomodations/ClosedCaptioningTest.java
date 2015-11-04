package tests.student.accomodations;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;
import util.ItemHandler;

import static org.junit.Assert.*;

/**
 * Created by emunoz on 11/4/15.
 */
public class ClosedCaptioningTest extends SeleniumBaseTest {
    private static final String CLOSED_CAPTIONING_SELECT_CSS_SELECTOR = "select[id*='-closedcaptioning']";

    private static final String CLOSED_CAPTIONING_OPTION = "TDS_ClosedCap1";

    @Before
    public void loginAndBeginTest() throws InterruptedException {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 8
        driver.findElement(By.cssSelector("option[value=\"8\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select test type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[2]")).click(); //Performance Test
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);
        //Enable closed captioning
        driver.findElement(By.cssSelector(
                CLOSED_CAPTIONING_SELECT_CSS_SELECTOR + " option[value='" + CLOSED_CAPTIONING_OPTION + "']")).click();

        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Sound check dialog
        driver.waitForAndGetElementByLocator(By.cssSelector(".sound_repeat")).click();
        driver.waitForAndGetElementByLocator(By.cssSelector(".playing_done"));
        driver.findElement(By.cssSelector("#btnSoundYes > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();
        do {
            ItemHandler.getAndHandleAssessmentItems(driver);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                navigator.clickDialogOkButton();
            }
        } while (!driver.isElementVisibleNow(By.cssSelector(".slides_container")));
    }

    @Test
    public void testClosedCaptioning() throws InterruptedException {
        final By ccLocator = By.className("tdsClosedCaptioning");
        assertFalse(driver.isElementVisibleNow(ccLocator));
        //Make sure pause/stop button is not visible
        assertFalse(driver.isElementVisibleNow(By.className("slide_controls_stop_btn")));

        //Start the slideshow video
        driver.findElement(By.className("slide_controls_play_btn")).click();

        //Make sure the closed captioning pane is set to active and is visible
        assertTrue(driver.findElement(ccLocator)
                .getAttribute("class").contains("tdsClosedCaptioningActive"));
        assertTrue(driver.isElementVisibleNow(By.className("slide_controls_stop_btn")));

        //Get the current caption's text <p> element
        WebElement ccCurrentTextEl = driver.findElement(By.cssSelector(".tdsClosedCaptioningActive p.currentText"));
        //Get the initial slide's "data-begin" value, which indicates when the slide starts.
        final String originalDataBeginVal = driver.findElement(By.cssSelector(".slide:not(.hidden)")).getAttribute("data-begin");
        final String originalCaption = ccCurrentTextEl.getText();

        assertFalse(originalCaption.isEmpty()); //Make sure CC text is present
        //Click the "next slide" button
        driver.findElement(By.className("slide_controls_next_btn")).click();
        //Get new "data-begin" value, which should be greater now that we skipped a slide.
        final String newDataBeginVal = driver.findElement(By.cssSelector(".slide:not(.hidden)")).getAttribute("data-begin");
        assertTrue(Integer.parseInt(newDataBeginVal) > Integer.parseInt(originalDataBeginVal));

        //Wait half a second for the closed caption tool to begin reading new caption, which should be different
        //than the first slide's caption..
        Thread.sleep(500);
        final String newCaption = driver.findElement(By.cssSelector(".tdsClosedCaptioningActive p.currentText")).getText();
        assertNotEquals(newCaption, originalCaption);

        //Click the "previous slide" button twice to reset to the beginning of the slide.
        driver.findElement(By.className("slide_controls_prev_btn")).click();
        //Wait another half second for closed caption tool to update
        Thread.sleep(500);
        assertEquals(originalDataBeginVal,
                driver.findElement(By.cssSelector(".slide:not(.hidden)")).getAttribute("data-begin"));
        //The original caption should now be re-read
        assertEquals(originalCaption,
                driver.findElement(By.cssSelector(".tdsClosedCaptioningActive p.currentText")).getText());

        //Stop the slideshow and ensure the caption tool is hidden again
        driver.findElement(By.className("slide_controls_stop_btn")).click();
        assertFalse(driver.isElementVisibleNow(ccLocator));
        assertFalse(driver.isElementVisibleNow(By.className("slide_controls_stop_btn")));
        assertTrue(driver.isElementVisibleNow(By.className("slide_controls_play_btn")));
    }
}
