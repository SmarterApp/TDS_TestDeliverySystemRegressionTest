package tests.student.designatedsupports;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by emunoz on 11/4/15.
 */
public class StackedTranslationsTest extends SeleniumBaseTest {
    private static final String LANGUAGE_SELECT_CSS_SELECTOR =  "select[id*='language']";

    private static final String SPANISH_LANGUAGE_OPTION = "ESN";

    // The following LANG_VALUE constants represent language "lang" attribute values for localized text.
    private static final String LANG_VALUE_SPANISH_MX = "es-mx";

    private static final String LANG_VALUE_ENGLISH_US = "en-us";

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
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        //Select Spanish language
        driver.findElement(By.cssSelector(
                LANGUAGE_SELECT_CSS_SELECTOR + " option[value='" + SPANISH_LANGUAGE_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();

        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();
    }

    @Test
    public void testStackedTranslations() {
        //Verify the language divider is present
        driver.findElement(By.cssSelector(".itemContainer.showing .languagedivider"));

        //Assert that both spanish and english translations are present for the assessment item
        assertTrue(driver.isElementVisibleNow(
                By.cssSelector(".itemContainer.showing .stemContainer p[lang='" + LANG_VALUE_SPANISH_MX + "']")));
        assertTrue(driver.isElementVisibleNow(
                By.cssSelector(".itemContainer.showing .stemContainer p[lang='" + LANG_VALUE_ENGLISH_US + "']")));
    }
}
