package tests.student.practicetest.general;

import driver.BrowserInteractionType;
import driver.impl.SmarterBalancedWebDriverImpl;
import enums.TestButton;
import enums.TestName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.StudentBaseTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 2/9/16.
 */
public class PauseResumeTest extends StudentBaseTest {

    /**
     * Summary:
     *
     * 1. Student logs in
     * 2. Student answers first page of questions
     * 3. Student pauses tests (and is logged out)
     * 4. Student logs in again (within 10 mins)
     * EXPECTED:
     * 5. Student is in page 2
     * 6. Student clicks back button
     * 7. Student can view page 1
     *
     * @throws Exception
     */
    @Test
    public void testReturnToQuestionAfterPause() throws Exception {
        driver.get(BASE_URL);
        navigator.login(STUDENT_SSID, STUDENT_FIRSTNAME, sessionId.split("-")[0], sessionId.split("-")[1], "");
        driver.waitForTitle("Is This You", true);
        assertEquals(STUDENT_SSID, driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[2]/span[2]")).getText());
        assertEquals(STUDENT_FIRSTNAME, driver.findElement(By.cssSelector("span.confirmData")).getText());
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        navigator.selectTest(TestName.IRP_GRADE_3_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Waiting for TA approval", true);
        assertEquals("Waiting for TA approval…", driver.findElement(By.id("sectionTestApprovalHeader")).getText());
        proctorApproveStudent(true);
        driver.waitForTitle("Is This Your Test", true);
        assertEquals(sessionId.toUpperCase(), driver.findElement(By.id("lblVerifySessionID")).getText());
        driver.findElement(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        assertEquals("Student: Login Shell Test Instructions and Help", driver.getTitle());
        assertEquals("Test Instructions and Help", driver.findElement(By.id("sectionInstructionsHeader")).getText());
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Answer questions, but stay on same page
        ItemHandler.getAndHandleAssessmentItems(driver, BrowserInteractionType.MOUSE);
        //Pause session
        navigator.clickButton(TestButton.PAUSE);
        assertEquals("Attention", driver.findElement(By.cssSelector("#yuiSimpleDialog h2")).getText());
        assertTrue(driver.findElement(By.cssSelector("#yuiSimpleDialog .bd")).getText().contains(
                "Are you sure you want to pause the test? If you pause your test for more than 10 minutes, " +
                        "you may be unable to make changes to questions that you have already answered."
        ));

        navigator.clickDialogYesButton();
        //Now at login page again
        driver.waitForTitle("Please Sign In", true);
        navigator.login(STUDENT_SSID, STUDENT_FIRSTNAME, sessionId.split("-")[0], sessionId.split("-")[1], "");
        driver.waitForTitle("Is This You", true);
        assertEquals(STUDENT_SSID, driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[2]/span[2]")).getText());
        assertEquals(STUDENT_FIRSTNAME, driver.findElement(By.cssSelector("span.confirmData")).getText());
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        navigator.selectTest(TestName.IRP_GRADE_3_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Waiting for TA approval", true);
        assertEquals("Waiting for TA approval…", driver.findElement(By.id("sectionTestApprovalHeader")).getText());
        proctorApproveStudent(true);
        driver.waitForTitle("Is This Your Test", true);
        assertEquals(sessionId.toUpperCase(), driver.findElement(By.id("lblVerifySessionID")).getText());
        driver.findElement(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        assertEquals("Student: Login Shell Test Instructions and Help", driver.getTitle());
        assertEquals("Test Instructions and Help", driver.findElement(By.id("sectionInstructionsHeader")).getText());
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
        driver.waitForTitle("Test, Page", true);
        assertTrue(driver.getTitle().contains("Page 2"));
        navigator.clickButton(TestButton.BACK);
        driver.waitForTitle("Page 1", true);
        assertTrue(driver.getTitle().contains("Page 1"));

    }

    @Test
    public void testReturnToQuestionAfterBrowserClose() throws Exception {
        driver.get(BASE_URL);
        navigator.login(STUDENT_SSID, STUDENT_FIRSTNAME, sessionId.split("-")[0], sessionId.split("-")[1], "");
        driver.waitForTitle("Is This You", true);
        assertEquals(STUDENT_SSID, driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[2]/span[2]")).getText());
        assertEquals(STUDENT_FIRSTNAME, driver.findElement(By.cssSelector("span.confirmData")).getText());
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        navigator.selectTest(TestName.IRP_GRADE_3_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Waiting for TA approval", true);
        assertEquals("Waiting for TA approval…", driver.findElement(By.id("sectionTestApprovalHeader")).getText());
        proctorApproveStudent(true);
        driver.waitForTitle("Is This Your Test", true);
        assertEquals(sessionId.toUpperCase(), driver.findElement(By.id("lblVerifySessionID")).getText());
        driver.findElement(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        assertEquals("Student: Login Shell Test Instructions and Help", driver.getTitle());
        assertEquals("Test Instructions and Help", driver.findElement(By.id("sectionInstructionsHeader")).getText());
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Answer questions, but stay on same page
        ItemHandler.getAndHandleAssessmentItems(driver, BrowserInteractionType.MOUSE);
        navigator.clickButton(TestButton.NEXT);
        //Browser is closed unexpectedly!
        driver.quit();

        initializeDriver();
        navigator.setDriver(driver);
        driver.get(BASE_URL);
        //Now at login page again
        driver.waitForTitle("Please Sign In", true);
        navigator.login(STUDENT_SSID, STUDENT_FIRSTNAME, sessionId.split("-")[0], sessionId.split("-")[1], "");
        driver.waitForTitle("Is This You", true);
        assertEquals(STUDENT_SSID, driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[2]/span[2]")).getText());
        assertEquals(STUDENT_FIRSTNAME, driver.findElement(By.cssSelector("span.confirmData")).getText());
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        navigator.selectTest(TestName.IRP_GRADE_3_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Waiting for TA approval", true);
        assertEquals("Waiting for TA approval…", driver.findElement(By.id("sectionTestApprovalHeader")).getText());
        proctorApproveStudent(true);
        driver.waitForTitle("Is This Your Test", true);
        assertEquals(sessionId.toUpperCase(), driver.findElement(By.id("lblVerifySessionID")).getText());
        driver.findElement(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        assertEquals("Student: Login Shell Test Instructions and Help", driver.getTitle());
        assertEquals("Test Instructions and Help", driver.findElement(By.id("sectionInstructionsHeader")).getText());
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
        driver.waitForTitle("Test, Page", true);
        assertTrue(driver.getTitle().contains("Page 2"));
        navigator.clickButton(TestButton.BACK);
        driver.waitForTitle("Page 1", true);
        assertTrue(driver.getTitle().contains("Page 1"));
    }

    @After
    public void endTest() throws Exception {
        navigator.clickButton(TestButton.NEXT);
        Thread.sleep(3000);
        do {
            ItemHandler.getAndHandleAssessmentItems(driver, BrowserInteractionType.MOUSE);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                navigator.clickDialogOkButton();
            }
        } while (navigator.isButtonAvailable(TestButton.NEXT) && !navigator.isButtonAvailable(TestButton.END));

        navigator.clickButton(TestButton.END);
        navigator.clickDialogYesButton();
        //Test ends - click submit
        driver.waitForAndFindElement(By.cssSelector("#btnCompleteTest button[type=\"button\"]")).click();
        //Submit test dialog confirmation
        navigator.clickDialogYesButton();
        driver.waitForTitle("Test Successfully Submitted", true);
        assertEquals("Test Successfully Submitted",
                driver.findElement(By.cssSelector("#sectionTestResultsHeader")).getText());
    }
}
