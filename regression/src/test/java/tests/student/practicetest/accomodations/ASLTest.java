package tests.student.practicetest.accomodations;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.StudentPracticeTestBaseTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by emunoz on 10/29/15.
 */
public class ASLTest extends StudentPracticeTestBaseTest {
    private static final String ASL_SELECT_CSS_SELECTOR = "select[id*='americansignlanguage']";
    private static final String ASL_ON_OPTION = "TDS_ASL1";

    @Before
    public void loginAndEnableAsl() throws Exception {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 3
        driver.findElement(By.cssSelector("option[value=\"12\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector(
                ASL_SELECT_CSS_SELECTOR + " option[value='" + ASL_ON_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(1000);
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(1000);
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(1000);
    }

    @Test
    public void testDragDialog() throws InterruptedException {
        driver.waitForTitle("Student: Test", true);
        navigator.selectOptionFromItemMenu("ASL");
        WebElement header = driver.findElement(By.cssSelector(".tool-video-container h2.hd"));
        Point headerLocation = header.getLocation();
        driver.findElement(By.cssSelector(".tool-video-container h2.hd"));
        Point moveTo = new Point(200, -100);
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
    public void testASL() {
        navigator.selectOptionFromItemMenu("ASL");
        // Assert that the video is open and visible
        driver.waitForAndFindElement(By.cssSelector(".tool-video-container div[id*='_jwplayer_display']"), 1);

        // Close ASL video by clicking close button on top right corner
        driver.findElement(By.cssSelector(".tool-video-container a.container-close")).click();

        //Assert the video is now closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".tool-video-container div[id*='_jwplayer_display']")));
    }
}
