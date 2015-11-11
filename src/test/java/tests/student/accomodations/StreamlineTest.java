package tests.student.accomodations;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.SeleniumBaseTest;
import util.ItemHandler;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by emunoz on 11/5/15.
 */
public class StreamlineTest extends SeleniumBaseTest {
    private static final String STREAMLINE_SELECT_CSS_SELECTOR = "select[id*='streamlinedmode']";
    private static final String STREAMLINE_ON_OPTION = "TDS_SLM1";
    private static final String STREAMLINED_CLASSNAME = "layout_wai";

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 12
        driver.findElement(By.cssSelector("option[value=\"12\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();

        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select test type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        //Enable streamlined mode
        driver.findElement(By.cssSelector(
                STREAMLINE_SELECT_CSS_SELECTOR + " option[value='" + STREAMLINE_ON_OPTION + "']")).click();

        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID")).getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
        driver.waitForTitle("Student: Test", true);
    }

    @Test
    public void testStreamlinedMode() throws InterruptedException {
        //The navigation pane and pageWrapper child div should contain the "layout_wai" class in streamlined mode
        String navigationClasses = driver.findElement(By.cssSelector("nav#navigation")).getAttribute("class");
        String pageWrapperClasses = driver.findElement(By.cssSelector(".pageWrapper.showing > div")).getAttribute("class");
        assertTrue(StringUtils.containsIgnoreCase(navigationClasses, STREAMLINED_CLASSNAME));
        assertTrue(StringUtils.containsIgnoreCase(pageWrapperClasses, STREAMLINED_CLASSNAME));
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#jumpGo")));
        //Streamlined mode should NOT contain the "bigTable" table element found in normal mode
        assertFalse(driver.isElementVisibleNow(By.cssSelector("table.bigTable")));

        //Streamlined mode contains a div with the class "response" that contains the question response content with a
        //child div with the class "optionGroup"
        assertTrue(driver.isElementVisibleNow(By.cssSelector("div.response > .optionGroup ")));

        //Answer the first page question(s)
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(1000);
        driver.waitForTitle("Page 2", true);

        //Navigate back using the "go" button after selecting the previous page from the dropdown.
        driver.findElement(By.cssSelector("#ddlNavigation option")).click();
        driver.findElement(By.cssSelector("#jumpGo")).click();

        //Ensure we are back on Page 1
        driver.waitForTitle("Page 1", true);
    }
}