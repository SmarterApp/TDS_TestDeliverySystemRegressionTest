package tests.student.practicetest.general;

import driver.BrowserInteractionType;
import enums.TestName;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.StudentPracticeTestBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the idle user timeout
 *
 * Created by emunoz on 10/21/15.
 */
public class UserTimeoutTest extends StudentPracticeTestBaseTest {

    /**
     * The time it takes for the timeout modal dialog to appear
     */
    private static final int TIMEOUT_TIME_MS = 610000;

    /**
     * The time it takes for the timeout modal dialog to close once it appears
     */
    private static final int TIMEOUT_MODAL_CLOSE_MS = 31000;

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Select grade level and continue
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();     //Select 3rd Grade
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.GRADE_3_MATH, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testTimeout() throws Exception {
        //Sleep 10 minutes.
        Thread.sleep(TIMEOUT_TIME_MS);

        assertTrue(navigator.isDialogShown());

        //Wait another 30 seconds for the dialog to disappear.
        Thread.sleep(TIMEOUT_MODAL_CLOSE_MS);

        //Back to login screen
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
    }

    @Test
    public void testTimeoutContinue() throws Exception {
        //Sleep 10 minutes.
        Thread.sleep(TIMEOUT_TIME_MS);

        //Ensure the timeout dialog is present
        assertTrue(navigator.isDialogShown());
        navigator.clickDialogYesButton();

        //Ensure the user wasn't kicked off to the login screen
        assertNotEquals("Student: Login Shell Please Sign In", driver.getTitle());
    }
}
