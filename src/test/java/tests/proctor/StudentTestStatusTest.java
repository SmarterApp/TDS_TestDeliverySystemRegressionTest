package tests.proctor;

import driver.BrowserInteractionType;
import enums.TestButton;
import enums.TestName;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.StudentBaseTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 2/12/16.
 */
public class StudentTestStatusTest extends StudentBaseTest {

    @Test
    public void testStudentTestProgress() throws Exception {
        assertEquals("0", proctorDriver.findElement(By.id("lblTotalOppsCount")).getText());
        driver.get(BASE_URL);
        navigator.login(STUDENT_SSID, STUDENT_FIRSTNAME, sessionId.split("-")[0], sessionId.split("-")[1], "");
        driver.waitForTitle("Is This You", true);
        assertEquals(STUDENT_SSID, driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[2]/span[2]")).getText());
        assertEquals(STUDENT_FIRSTNAME, driver.findElement(By.cssSelector("span.confirmData")).getText());
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        navigator.selectTest(TestName.GRADE_3_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Waiting for TA approval", true);
        assertEquals("Waiting for TA approval…", driver.findElement(By.id("sectionTestApprovalHeader")).getText());
        proctorApproveStudent(false); //Test approving a specific student (vs approve all)
        driver.waitForTitle("Is This Your Test", true);
        //Proctor side should see 1 student in progress
        assertEquals("1", proctorDriver.findElement(By.id("lblTotalOppsCount")).getText());
        assertEquals(STUDENT_SSID, proctorDriver.findElement(By.cssSelector("td.table_ssid")).getText());
        assertTrue(proctorDriver.findElement(By.cssSelector("td.table_status span")).getText().startsWith("approved: 0/"));
        assertEquals(sessionId.toUpperCase(), driver.findElement(By.id("lblVerifySessionID")).getText());
        driver.findElement(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        assertEquals("Student: Login Shell Test Instructions and Help", driver.getTitle());
        assertEquals("Test Instructions and Help", driver.findElement(By.id("sectionInstructionsHeader")).getText());
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

        //Student takes test
        do {
            ItemHandler.getAndHandleAssessmentItems(driver, BrowserInteractionType.MOUSE);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                navigator.clickDialogOkButton();
            }
        } while (navigator.isButtonAvailable(TestButton.NEXT) && !navigator.isButtonAvailable(TestButton.END));

        navigator.clickButton(TestButton.END);
        navigator.clickDialogYesButton();

        //Refresh proctor page
        proctorDriver.findElement(By.id("btnRefresh")).click();
        Thread.sleep(1000);
        //In review page
        assertTrue(proctorDriver.findElement(By.cssSelector("td.table_status span")).getText().startsWith("review"));
        //Test ends - click submit
        driver.waitForAndFindElement(By.cssSelector("#btnCompleteTest button[type=\"button\"]")).click();
        //Submit test dialog confirmation
        navigator.clickDialogYesButton();
        driver.waitForTitle("Test Successfully Submitted", true);
        assertEquals("Test Successfully Submitted",
                driver.findElement(By.cssSelector("#sectionTestResultsHeader")).getText());

        //Refresh proctor page
        proctorDriver.findElement(By.id("btnRefresh")).click();
        Thread.sleep(1000);
        //In review page
        assertTrue(proctorDriver.findElement(By.cssSelector("td.table_status span")).getText().startsWith("completed"));
    }
}
