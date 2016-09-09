package tests.student.practicetest.general;

import driver.BrowserInteractionType;
import enums.TestButton;
import enums.TestName;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.StudentPracticeTestBaseTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

/**
 * Created by emunoz on 2/15/16.
 */
public class ReviewTest extends StudentPracticeTestBaseTest {
    /**
     * The amount of time to wait between next assessment item page navigation
     */
    private static final int NEXT_PAGE_WAIT_IN_MS = 1000;

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

    @Test
    public void testReviewQuestions() throws Exception {
        do {
            ItemHandler.getAndHandleAssessmentItems(driver, BrowserInteractionType.MOUSE);
            navigator.clickNextButtonAndWait(NEXT_PAGE_WAIT_IN_MS);

            if (navigator.isDialogShown()) {
                assertEquals("Attention", driver.findElement(By.cssSelector("#yuiSimpleDialog h2")).getText());
                assertTrue(driver.findElement(By.cssSelector("#yuiSimpleDialog .bd")).getText().contains(
                        "You have answered all the questions in this test. When you have finished checking your answers, " +
                                "click the [End Test] button."
                ));
                navigator.clickDialogOkButton();
            }
        } while (navigator.isButtonAvailable(TestButton.NEXT) && !navigator.isButtonAvailable(TestButton.END));

        navigator.clickButton(TestButton.END);
        assertEquals("Attention", driver.findElement(By.cssSelector("#yuiSimpleDialog h2")).getText());
        assertTrue(driver.findElement(By.cssSelector("#yuiSimpleDialog .bd")).getText().contains(
                "You have reached the end of the test. Click [Yes] to continue to the next page. Click [No] to keep working on your test."
        ));
        navigator.clickDialogYesButton();
        //On review page
        driver.waitForAndFindElement(By.linkText("3")).click();
        driver.waitForTitle("Page 2", true);
        driver.isElementVisibleNow(By.id("QuestionNumber_3"));

        navigator.clickButton(TestButton.END);
        navigator.clickDialogYesButton();
        //Test ends - click submit
        driver.waitForAndFindElement(By.cssSelector("#btnCompleteTest button[type=\"button\"]")).click();
        assertEquals("Warning", driver.findElement(By.cssSelector("#yuiSimpleDialog h2")).getText());
        assertTrue(driver.findElement(By.cssSelector("#yuiSimpleDialog .bd")).getText().contains(
                "Are you sure you want to submit the test?"
        ));
        //Submit test dialog confirmation
        navigator.clickDialogYesButton();
        driver.waitForTitle("Test Successfully Submitted", true);
        assertEquals("Test Successfully Submitted",
                driver.findElement(By.cssSelector("#sectionTestResultsHeader")).getText());
    }
}
