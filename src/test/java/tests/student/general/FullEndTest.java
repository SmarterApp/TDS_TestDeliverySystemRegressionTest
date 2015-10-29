package tests.student.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;
import util.ItemSelector;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

/**
 * Created by emunoz on 10/20/15.
 */
public class FullEndTest extends SeleniumBaseTest {

    @Before
    public void openHomeAndLogin() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();
    }

    @After
    public void endTest() {
        navigator.clickEndTestButton();
        navigator.clickDialogYesButton();
        //Test ends - click submit
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnCompleteTest button[type=\"button\"]")).click();
        //Submit test dialog confirmation
        navigator.clickDialogYesButton();
        driver.waitForTitleAndAssert("Test Successfully Submitted", true);
        assertEquals("Test Successfully Submitted",
                driver.findElement(By.cssSelector("#sectionTestResultsHeader")).getText());
    }

    @Test
    public void testGrade3Math35TrainingTest() throws Exception {

        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                navigator.clickDialogOkButton();
            }
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());
    }

    @Test
    public void testGrade3MathPracticeTest() throws Exception {

        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[3]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                navigator.clickDialogOkButton();
            }
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());
    }

    @Test
    public void testGrade3MathPerformanceTest() throws Exception {

        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[5]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                navigator.clickDialogOkButton();
            }
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());
    }

    @Test
    public void testGrade3ELAPracticeTest() throws Exception {
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();     //Select 3rd grade
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[4]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Sound check dialog
        driver.waitForAndGetElementByLocator(By.cssSelector(".sound_repeat")).click();
        driver.waitForAndGetElementByLocator(By.cssSelector(".playing_done"));
        driver.findElement(By.cssSelector("#btnSoundYes > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                navigator.clickDialogOkButton();
            }
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());
    }

    @Test
    public void testGrade12ELAPerformanceTest() throws Exception {
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();     //Select 3rd Grade
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[6]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());

        //Sound check dialog
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        driver.waitForAndGetElementByLocator(By.cssSelector(".sound_repeat")).click();
        driver.waitForAndGetElementByLocator(By.cssSelector(".playing_done"));
        driver.findElement(By.cssSelector("#btnSoundYes > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                navigator.clickDialogOkButton();
            }
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());
    }

    @Test
    public void testGrade12MathTest() throws Exception {
        driver.findElement(By.cssSelector("option[value=\"12\"]")).click();     //Select 12th Grade
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                navigator.clickDialogOkButton();
            }
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());

    }

    @Test
    public void testGrade12ELAHSTest() throws Exception {
        driver.findElement(By.cssSelector("option[value=\"12\"]")).click();     //Select 12th Grade
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[2]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());

        //Sound check dialog
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        driver.waitForAndGetElementByLocator(By.cssSelector(".sound_repeat")).click();
        driver.waitForAndGetElementByLocator(By.cssSelector(".playing_done"));
        driver.findElement(By.cssSelector("#btnSoundYes > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                navigator.clickDialogOkButton();
            }
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());

    }
}
