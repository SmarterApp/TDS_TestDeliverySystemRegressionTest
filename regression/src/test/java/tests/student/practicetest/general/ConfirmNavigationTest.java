package tests.student.practicetest.general;

import driver.BrowserInteractionType;
import enums.TestName;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import tests.StudentPracticeTestBaseTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

/**
 * Created by emunoz on 2/17/16.
 */
public class ConfirmNavigationTest extends StudentPracticeTestBaseTest {
    private static final String CONFIRM_DIALOG_MESSAGE =
            "This page is asking you to confirm that you want to leave - data you have entered may not be saved.";

    @Before
    public void openHomeAndLogin() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();
        //Select 3rd grade
        driver.findElement(cssSelector("option[value=\"12\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button"), BrowserInteractionType.MOUSE).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);
        navigator.selectTest(TestName.HIGH_SCHOOL_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        // Continue with default settings
        driver.findElement(cssSelector("#btnAccSelect button"), BrowserInteractionType.MOUSE).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(cssSelector("#btnStartTest button"), BrowserInteractionType.MOUSE).click();

    }

    /**
     * Tests that a confirmation dialog is presented when a student attempts to navigate away from a test that is in progress.
     */
    @Test
    public void testConfirmAwayDialog() {
        ItemHandler.getAndHandleAssessmentItems(driver);
        driver.get(BASE_URL);
        Alert confirmationDlg = driver.switchTo().alert();
        assertEquals(CONFIRM_DIALOG_MESSAGE, confirmationDlg.getText());
        confirmationDlg.dismiss(); //cancel - stay on test
        assertTrue(driver.getTitle().contains("Test, Page 1"));

        driver.get(BASE_URL);
        confirmationDlg = driver.switchTo().alert();
        assertEquals(CONFIRM_DIALOG_MESSAGE, confirmationDlg.getText());
        confirmationDlg.accept();
        assertTrue(driver.getTitle().contains("Login Shell Please Sign In"));
    }
}
