package tests.student.universaltools;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 11/3/15.
 */
public class ExpandablePassagesTest extends SeleniumBaseTest {
    private static final String PASSAGE_COLLAPSED_CLASS = "passage-collapsed";

    private static final String PASSAGE_EXPANDED_CLASS = "passage-expanded";

    private static final String MASK_QUESTIONS_CLASS = "maskQuestions";

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");
        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 12
        driver.findElement(By.cssSelector("option[value=\"12\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[2]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);
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
    }

    @Test
    public void testExpandablePassage() throws InterruptedException {
        driver.waitForTitleAndAssert("Student: Test", true);
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
