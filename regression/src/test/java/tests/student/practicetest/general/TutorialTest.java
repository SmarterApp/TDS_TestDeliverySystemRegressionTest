package tests.student.practicetest.general;

import com.googlecode.zohhak.api.TestWith;
import driver.BrowserInteractionType;
import enums.TestName;
import org.junit.Before;
import org.openqa.selenium.By;
import tests.StudentPracticeTestBaseTest;

import static org.junit.Assert.*;

/**
 * Created by emunoz on 11/4/15.
 */
public class TutorialTest extends StudentPracticeTestBaseTest {

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 12
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.GRADES_3_TO_5_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
    }

    //@TestWith({"KEYBOARD", "MOUSE"})
    @TestWith({"MOUSE"})
    public void testTutorial(BrowserInteractionType interactionTypes) {
        //Assert the tutorial window is closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#dialogs #dialogContent")));

        //Open tutorial
        navigator.selectOptionFromItemMenu("helpItem");

        //Assert the tutorial window is now opened
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#dialogs #dialogContent")));
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#dialogContent.enable")));

        //Switch into the dialog's iframe
        driver.switchToIframe(By.cssSelector("#dialogContent.enable iframe"));
        assertTrue(driver.isElementVisibleNow(By.className("showingDialog")));

        //Switch into the HTML5 tutorial video's iframe
        driver.switchToIframe(By.cssSelector("iframe.frame-anim-html5"));

        //Verify canvas is visible and <audio> element exists
        assertTrue(driver.isElementVisibleNow(By.cssSelector("canvas#canvas")));
        assertNotNull(driver.findElement(By.cssSelector("audio#mySound")));
        driver.switchOutOfIFrame();

        //Close the tutorial window
        driver.findElement(By.cssSelector("#dialogContent #dialogContentClose a")).click();
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#dialogs #dialogContent")));
    }
}
