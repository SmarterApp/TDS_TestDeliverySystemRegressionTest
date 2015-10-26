package tests.student.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOG = LogManager.getLogger(FullEndTest.class);

    @Test
    public void testGrade3Math35TrainingTest() throws Exception {
        LOG.info("Starting SmarterBalanced practice test run at {}", new Timestamp(System.currentTimeMillis()));
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        navigator.loginAsGuest();
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();     //Select 12th Grade
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        waitHelper.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        waitHelper.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = waitHelper.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        waitHelper.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver); //Q8 - Q15
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                LOG.info("Dialog detected. Clicking ok button...");
                navigator.clickDialogOkButton();
            }
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());

        //Confirm last questions
        navigator.clickEndTestButton();
        navigator.clickDialogYesButton();
        //Test ends - click submit
        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnCompleteTest button[type=\"button\"]")).click();
        //Submit test dialog confirmation
        navigator.clickDialogYesButton();
        waitHelper.waitForTitleAndAssert("Test Successfully Submitted", true);
        assertEquals("Test Successfully Submitted",
                driver.findElement(By.cssSelector("#sectionTestResultsHeader")).getText());
    }

    @Test
    public void testGrade3ELAPracticeTest() throws Exception {
        LOG.info("Starting SmarterBalanced practice test run at {}", new Timestamp(System.currentTimeMillis()));
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        navigator.loginAsGuest();
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();     //Select 12th Grade
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        waitHelper.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[4]")).click();
        waitHelper.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = waitHelper.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Sound check dialog
        waitHelper.waitForAndGetElementByLocator(By.cssSelector(".sound_repeat")).click();
        waitHelper.waitForAndGetElementByLocator(By.cssSelector(".playing_done"));
        driver.findElement(By.cssSelector("#btnSoundYes > span > button[type=\"button\"]")).click();

        //Instructions
        waitHelper.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver);
            navigator.clickNextButtonAndWait(1000);

            if (navigator.isDialogShown()) {
                LOG.info("Dialog detected. Clicking ok button...");
                navigator.clickDialogOkButton();
            }
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());

        //Confirm last questions
        navigator.clickEndTestButton();
        navigator.clickDialogYesButton();
        //Test ends - click submit
        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnCompleteTest button[type=\"button\"]")).click();
        //Submit test dialog confirmation
        navigator.clickDialogYesButton();
        waitHelper.waitForTitleAndAssert("Test Successfully Submitted", true);
        assertEquals("Test Successfully Submitted",
                driver.findElement(By.cssSelector("#sectionTestResultsHeader")).getText());
    }

    @Test
    public void testGrade12ELAPerformanceTest() throws Exception {
        LOG.info("Starting SmarterBalanced practice test run at {}", new Timestamp(System.currentTimeMillis()));
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();     //Select 12th Grade
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        waitHelper.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[6]")).click();
        waitHelper.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = waitHelper.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());

        //Sound check dialog
        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        waitHelper.waitForAndGetElementByLocator(By.cssSelector(".sound_repeat")).click();
        waitHelper.waitForAndGetElementByLocator(By.cssSelector(".playing_done"));
        driver.findElement(By.cssSelector("#btnSoundYes > span > button[type=\"button\"]")).click();

        //Instructions
        waitHelper.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver); //Q8 - Q15
            navigator.clickNextButtonAndWait(1000);
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());

        //Confirm last questions
        navigator.clickDialogOkButton();
        navigator.clickEndTestButton();
        navigator.clickDialogYesButton();
        //Test ends - click submit
        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnCompleteTest button[type=\"button\"]")).click();
        //Submit test dialog confirmation
        navigator.clickDialogYesButton();
        waitHelper.waitForTitleAndAssert("Test Successfully Submitted", true);
        assertEquals("Test Successfully Submitted",
                driver.findElement(By.cssSelector("#sectionTestResultsHeader")).getText());
    }

    @Test
    public void testGrade12MathTest() throws Exception {
        LOG.info("Starting SmarterBalanced practice test run at {}", new Timestamp(System.currentTimeMillis()));
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
        driver.findElement(By.cssSelector("option[value=\"12\"]")).click();     //Select 12th Grade
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        waitHelper.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        waitHelper.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = waitHelper.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        //Instructions
        waitHelper.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();
        //waitHelper.waitForAndGetElementByLocator(By.xpath("/*[name()='object']/*[name()='image']"));
        //waitHelper.waitForAndGetElementByLocator(By.xpath("//object/*[name()='svg']"));
        Thread.sleep(45000);
        WebElement mapObject = (WebElement) ((JavascriptExecutor)driver).executeScript("return document.querySelector(arguments[0])", "svg rect");

        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {view: window, bubbles:true, cancelable: true}))", mapObject);


    }

    @Test
    public void testGrade12ELAHSTest() throws Exception {
        LOG.info("Starting SmarterBalanced practice test run at {}", new Timestamp(System.currentTimeMillis()));
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
        driver.findElement(By.cssSelector("option[value=\"12\"]")).click();     //Select 12th Grade
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        waitHelper.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[2]")).click();
        waitHelper.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = waitHelper.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());

        //Sound check dialog
        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
        waitHelper.waitForAndGetElementByLocator(By.cssSelector(".sound_repeat")).click();
        waitHelper.waitForAndGetElementByLocator(By.cssSelector(".playing_done"));
        driver.findElement(By.cssSelector("#btnSoundYes > span > button[type=\"button\"]")).click();

        //Instructions
        waitHelper.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Test begins
        do {
            ItemSelector.getAssessmentItemsFromPage(driver); //Q8 - Q15
            navigator.clickNextButtonAndWait(1000);
        } while (navigator.nextButtonAvailable() && !navigator.endButtonAvailable());

        //Confirm last questions
        navigator.clickDialogOkButton();
        navigator.clickEndTestButton();
        navigator.clickDialogYesButton();
        //Test ends - click submit
        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnCompleteTest button[type=\"button\"]")).click();
        //Submit test dialog confirmation
        navigator.clickDialogYesButton();
        waitHelper.waitForTitleAndAssert("Test Successfully Submitted", true);
        assertEquals("Test Successfully Submitted",
                driver.findElement(By.cssSelector("#sectionTestResultsHeader")).getText());
    }
}
