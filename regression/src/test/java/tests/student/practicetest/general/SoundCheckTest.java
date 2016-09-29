package tests.student.practicetest.general;

import driver.BrowserInteractionType;
import enums.TestName;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.StudentPracticeTestBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

/**
 * Created by emunoz on 2/10/16.
 */
public class SoundCheckTest extends StudentPracticeTestBaseTest {
    @Before
    public void openHomeAndLogin() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();
    }

    @Test
    public void testWorkingSoundCheck() {
        //Select 3rd grade
        driver.findElement(cssSelector("option[value=\"3\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button"), BrowserInteractionType.MOUSE).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);
        navigator.selectTest(TestName.GRADES_3_TO_5_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        // Continue with default settings
        driver.findElement(cssSelector("#btnAccSelect button"), BrowserInteractionType.MOUSE).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();
        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
    }

    @Test
    public void testSoundCheckFailure() {
        //Select 3rd grade
        driver.findElement(cssSelector("option[value=\"3\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button"), BrowserInteractionType.MOUSE).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);
        navigator.selectTest(TestName.GRADES_3_TO_5_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        // Continue with default settings
        driver.findElement(cssSelector("#btnAccSelect button"), BrowserInteractionType.MOUSE).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(cssSelector("#btnApproveAccommodations button")).click();

        driver.waitForAndFindElement(By.cssSelector(".sound_repeat")).click();
        driver.waitForAndFindElement(By.cssSelector(".playing_done"));
        driver.findElement(By.cssSelector("#btnSoundNo button")).click();
        assertEquals("Sound Check: Audio Problem", driver.findElement(By.cssSelector("#checkError h1")).getText());
        assertEquals("Tell the Test Administrator that you have an audio problem. Playing and recording sound is required for this test.",
                driver.findElement(By.cssSelector("#checkError .instructions p")).getText());

    }
}
