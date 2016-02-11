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

/**
 * Created by emunoz on 11/2/15.
 */
public class MarkForReviewTest extends StudentPracticeTestBaseTest {
    private static final String MARK_FOR_REVIEW_CLASS = "markReview";

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 12
        driver.findElement(By.cssSelector("option[value=\"12\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.HIGH_SCHOOL_MATH, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testMarkForReview() {
        driver.waitForTitle("Student: Test", true);
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".showing.itemMarked")));

        navigator.selectOptionFromItemMenu(MARK_FOR_REVIEW_CLASS);
        //Ensure that the item div is marked as "itemMarked"
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".showing.itemMarked")));

        //Ensure that the navigation select element shows the current question as "marked"
        assertTrue(driver.findElement(By.cssSelector("select#ddlNavigation")).getText().contains("(marked)"));

        //Unmark the question
        navigator.selectOptionFromItemMenu(MARK_FOR_REVIEW_CLASS);
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".showing.itemMarked")));
        assertFalse(driver.findElement(By.cssSelector("select#ddlNavigation")).getText().contains("(marked)"));
    }
}
