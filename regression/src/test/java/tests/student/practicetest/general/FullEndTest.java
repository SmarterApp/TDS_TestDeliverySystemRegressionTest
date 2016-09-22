package tests.student.practicetest.general;

import com.googlecode.zohhak.api.TestWith;
import driver.BrowserInteractionType;
import enums.TestButton;
import enums.TestName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import tests.StudentPracticeTestBaseTest;
import tests.categories.StudentGuestTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.*;

/**
 * Created by emunoz on 10/20/15.
 */
public class FullEndTest extends StudentPracticeTestBaseTest {
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
    }

    /**
     * This method iterates through each test assessment page until the test is complete or an exception occurs.
     * This process includes handling each individual item, clicking the next button, and checking for dialogs (and
     * closing them appropriately).
     *
     * @throws InterruptedException
     */
    private void takeTest(BrowserInteractionType interactionType) throws InterruptedException {
        do {
            ItemHandler.getAndHandleAssessmentItems(driver, interactionType);
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
    }

    @After
    public void endTest() {
        navigator.clickButton(TestButton.END);
        assertEquals("Attention", driver.findElement(By.cssSelector("#yuiSimpleDialog h2")).getText());
        assertTrue(driver.findElement(By.cssSelector("#yuiSimpleDialog .bd")).getText().contains(
            "You have reached the end of the test. Click [Yes] to continue to the next page. Click [No] to keep working on your test."
        ));
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

    @Category(StudentGuestTest.class)
    //@TestWith({"MOUSE", "KEYBOARD"})
    @TestWith("MOUSE")
    public void testGrades3_5ELATest(BrowserInteractionType interactionType) throws Exception {
        //Select 3rd grade
        driver.findElement(cssSelector("option[value=\"3\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button"), interactionType).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);
        navigator.selectTest(TestName.GRADES_3_TO_5_ELA, interactionType);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        // Continue with default settings
        driver.findElement(cssSelector("#btnAccSelect button"), interactionType).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(cssSelector("#btnStartTest button"), interactionType).click();

        //Test begins
        takeTest(interactionType);
    }

    @Category(StudentGuestTest.class)
    //@TestWith({"MOUSE", "KEYBOARD"})
    @TestWith("MOUSE")
    public void testGrade3ELATest(BrowserInteractionType interactionType) throws Exception {

        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.GRADE_3_ELA, interactionType);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Test begins
        takeTest(interactionType);
    }

    @TestWith("MOUSE")
    public void testGradePerf3ELATest(BrowserInteractionType interactionType) throws Exception {

        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.PERF_GRADE_3_ELA, interactionType);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Test begins
        takeTest(interactionType);
    }

    @TestWith("MOUSE")
    public void testGradePerf3MathTest(BrowserInteractionType interactionType) throws Exception {

        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.PERF_GRADE_3_MATH, interactionType);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Test begins
        takeTest(interactionType);
    }

    @Category(StudentGuestTest.class)
    //@TestWith({"MOUSE", "KEYBOARD"})
    @TestWith("MOUSE")
    public void testGrade3IRP_ELATest(BrowserInteractionType interactionType) throws Exception {
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.IRP_GRADE_3_ELA, interactionType);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();
        //navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Test begins
        takeTest(interactionType);
    }

    @Category(StudentGuestTest.class)
    //@TestWith({"MOUSE", "KEYBOARD"})
    @TestWith("MOUSE")
    public void testGrades3_5MathTest(BrowserInteractionType interactionType) throws Exception {

        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.GRADES_3_TO_5_MATH, interactionType);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Test begins
        takeTest(interactionType);
    }


    @Test
    public void testGrades6_8ELA() throws Exception {
        driver.findElement(By.cssSelector("option[value=\"6\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.GRADES_6_TO_8_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Test begins
        takeTest(BrowserInteractionType.MOUSE);
    }

    @Test
    public void testGrade7ELA() throws Exception {
        driver.findElement(By.cssSelector("option[value=\"7\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.GRADE_7_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();
        //navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Test begins
        takeTest(BrowserInteractionType.MOUSE);
    }
}
