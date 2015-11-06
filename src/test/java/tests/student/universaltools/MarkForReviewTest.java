package tests.student.universaltools;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 11/2/15.
 */
public class MarkForReviewTest extends SeleniumBaseTest {
    private static final String MARK_FOR_REVIEW_CLASS = "markReview";

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 11
        driver.findElement(By.cssSelector("option[value=\"11\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID")).getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testMarkForReview() {
        driver.waitForTitleAndAssert("Student: Test", true);
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
