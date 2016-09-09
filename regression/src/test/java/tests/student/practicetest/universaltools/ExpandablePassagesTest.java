package tests.student.practicetest.universaltools;

import driver.BrowserInteractionType;
import enums.TestName;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.StudentPracticeTestBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.*;

/**
 * Created by emunoz on 11/3/15.
 */
public class ExpandablePassagesTest extends StudentPracticeTestBaseTest {
    private static final String PASSAGE_COLLAPSED_CLASS = "passage-collapsed";
    private static final String PASSAGE_EXPANDED_CLASS = "passage-expanded";
    private static final String MASK_QUESTIONS_CLASS = "maskQuestions";

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL);
        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 12
        driver.findElement(cssSelector("option[value=\"12\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.HIGH_SCHOOL_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testExpandablePassage() throws InterruptedException {
        driver.waitForTitle("Student: Test", true);
        assertTrue(driver.isElementVisibleNow(By.className(PASSAGE_COLLAPSED_CLASS)));
        assertFalse(driver.isElementVisibleNow(By.className(PASSAGE_EXPANDED_CLASS)));
        assertFalse(driver.isElementVisibleNow(By.className(MASK_QUESTIONS_CLASS)));
        Double collapsedPassageWidth = convertPxToDouble(driver.findElement(By.cssSelector(".showing.thePassage"))
                .getCssValue("width"));

        //Expand passage
        driver.findElement(By.cssSelector(".showing .expand-collapse-passage")).click();

        assertFalse(driver.isElementVisibleNow(By.className(PASSAGE_COLLAPSED_CLASS)));
        assertTrue(driver.isElementVisibleNow(By.className(PASSAGE_EXPANDED_CLASS)));
        Double expandedPassageWidth = convertPxToDouble(driver.findElement(By.cssSelector(".showing.thePassage"))
                .getCssValue("width"));

        assertTrue(driver.isElementVisibleNow(By.className(MASK_QUESTIONS_CLASS)));
        assertEquals("0.95", driver.findElement(By.className(MASK_QUESTIONS_CLASS)).getCssValue("opacity"));

        //Ensure widths were adjusted appropriately
        assertTrue(collapsedPassageWidth < expandedPassageWidth);

        //Collapse passage
        driver.findElement(By.cssSelector(".showing .expand-collapse-passage")).click();
        Thread.sleep(500); //Wait half a second for the animation to occur and width to update
        driver.findElement(By.className(PASSAGE_COLLAPSED_CLASS));

        //Ensure the collapsed width is the same as it was originally
        assertEquals(collapsedPassageWidth,
                convertPxToDouble(driver.findElement(By.cssSelector(".showing.thePassage")).getCssValue("width")));
        assertFalse(driver.isElementVisibleNow(By.className(MASK_QUESTIONS_CLASS)));
    }

    private static Double convertPxToDouble(String pxStr) {
        return Double.parseDouble(pxStr.replace("px", ""));
    }
}
