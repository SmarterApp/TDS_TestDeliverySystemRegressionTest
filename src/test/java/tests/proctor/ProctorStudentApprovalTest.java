package tests.proctor;

import driver.BrowserInteractionType;
import enums.TestName;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.StudentBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 2/15/16.
 */
public class ProctorStudentApprovalTest extends StudentBaseTest {
    @Test
    public void testDenyStudent() throws Exception {
        assertEquals("0", proctorDriver.findElement(By.id("lblTotalOppsCount")).getText());
        driver.get(BASE_URL);
        navigator.login(STUDENT_SSID, STUDENT_FIRSTNAME, sessionId.split("-")[0], sessionId.split("-")[1], "");
        driver.waitForTitle("Is This You", true);
        assertEquals(STUDENT_SSID, driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[2]/span[2]")).getText());
        assertEquals(STUDENT_FIRSTNAME, driver.findElement(By.cssSelector("span.confirmData")).getText());
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        navigator.selectTest(TestName.IRP_GRADE_3_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Waiting for TA approval", true);
        assertEquals("Waiting for TA approval…", driver.findElement(By.id("sectionTestApprovalHeader")).getText());
        final String denyText = "Wrong test";
        proctorDenyStudent(denyText);
        assertTrue(driver.findElement(By.cssSelector("#yuiSimpleDialog .bd")).getText().contains(denyText));
        navigator.clickDialogOkButton();
        driver.waitForTitle("Please Sign In", true);
    }
}
