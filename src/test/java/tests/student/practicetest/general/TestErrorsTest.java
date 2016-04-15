package tests.student.practicetest.general;

import driver.BrowserInteractionType;
import enums.TestButton;
import enums.TestName;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.StudentPracticeTestBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

/**
 * Created by emunoz on 2/10/16.
 */
public class TestErrorsTest extends StudentPracticeTestBaseTest {
    private static final String INVALID_DATA_TYPE_ERROR = "This cell only accepts valid numerical data.";

    private static final String ANSWER_REQUIRED_ERROR =
            "You must answer all questions on this page before moving to the next page.";

    @Before
    public void openHomeAndLogin() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();
    }

    /**
     * Tests that typing a non-numerical key into a numerical field produces an error warning.
     * @throws Exception
     */
    @Test
    public void  testInvalidInput() throws Exception {
        //Select 3rd grade
        driver.findElement(cssSelector("option[value=\"3\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button"), BrowserInteractionType.MOUSE).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);
        navigator.selectTest(TestName.GRADE_3_MATH, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        // Continue with default settings
        driver.findElement(cssSelector("#btnAccSelect button"), BrowserInteractionType.MOUSE).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(cssSelector("#btnStartTest button"), BrowserInteractionType.MOUSE).click();

        driver.waitForTitle("Test, Page", true);

        WebElement numericInput = driver.findElement(By.cssSelector(".format_ti .answerContainer input.ti-input"));
        numericInput.sendKeys("a");

        assertTrue(driver.findElement(By.cssSelector("#yuiSimpleDialog .bd")).getText().contains(INVALID_DATA_TYPE_ERROR));
        navigator.clickDialogOkButton();
    }

    /**
     * Tests that an error is presented if no answer is selected on the page.
     */
    @Test
    public void testNoAnswerNextButton() throws Exception {
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
        driver.findElement(cssSelector("#btnStartTest button"), BrowserInteractionType.MOUSE).click();

        driver.waitForTitle("Test, Page", true);
        navigator.clickButton(TestButton.NEXT);
        Thread.sleep(3000);

        assertEquals("Attention", driver.findElement(By.cssSelector("#yuiSimpleDialog h2")).getText());
        assertTrue(driver.findElement(By.cssSelector("#yuiSimpleDialog .bd")).getText().contains(
                ANSWER_REQUIRED_ERROR
        ));
    }
}
