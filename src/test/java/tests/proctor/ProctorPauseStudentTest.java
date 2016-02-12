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
public class ProctorPauseStudentTest extends StudentBaseTest {
    private static final String PAUSE_MSG =
        "If you pause this test, the student will be logged out. If the student does not log back in within 20 minutes, " +
                "he or she may be unable to see the questions that were already answered. Are you sure you want to pause this test?";

    @Test
    public void testPauseStudent() throws Exception {
        driver.get(BASE_URL);
        navigator.login(STUDENT_SSID, STUDENT_FIRSTNAME, sessionId.split("-")[0], sessionId.split("-")[1], "");
        driver.waitForTitle("Is This You", true);
        assertEquals(STUDENT_SSID, driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[2]/span[2]")).getText());
        assertEquals(STUDENT_FIRSTNAME, driver.findElement(By.cssSelector("span.confirmData")).getText());
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        navigator.selectTest(TestName.GRADE_3_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Waiting for TA approval", true);
        assertEquals("Waiting for TA approval…", driver.findElement(By.id("sectionTestApprovalHeader")).getText());
        proctorApproveStudent();
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

        //Click pause button
        proctorDriver.findElement(By.cssSelector("td.table_pause a")).click();
        assertEquals("Important!", proctorDriver.findElement(By.cssSelector("#msgDialog h2")).getText());
        assertEquals(PAUSE_MSG, proctorDriver.findElement(By.cssSelector("#msgDialog .message_container")).getText());
        proctorDriver.findElement(By.cssSelector(".dialogs .action a.confirm")).click();
        Thread.sleep(2000);
        assertTrue(proctorDriver.findElement(By.cssSelector("td.table_status span")).getText().startsWith("paused"));
    }
}
