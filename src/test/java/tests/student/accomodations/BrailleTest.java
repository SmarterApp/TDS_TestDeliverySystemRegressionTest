package tests.student.accomodations;

import org.junit.Before;
import org.junit.Test;
import tests.SeleniumBaseTest;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;

/**
 * Created by emunoz on 11/10/15.
 */
public class BrailleTest extends SeleniumBaseTest {

    private static final String LANGUAGE_SELECT_CSS_SELECTOR = "select[id*='language']";

    private static final String EMBOSS_REQ_TYPE_SELECT_CSS_SELECTOR = "select[id*='embossrequesttype']";

    private static final String BRAILLE_LANGUAGE_OPTION = "ENU-Braille";

    private static final String ON_REQ_OPTION = "TDS_ERT_OR";

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 12
        driver.findElement(cssSelector("option[value=\"12\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select test type
        driver.findElement(xpath("//ul[@id='testSelections']/li[2]")).click(); //Performance Test
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);

        //Enable Braille
        driver.findElement(cssSelector(
                LANGUAGE_SELECT_CSS_SELECTOR + " option[value='" + BRAILLE_LANGUAGE_OPTION + "']")).click();

        //Emboss option on-request
        driver.findElement(cssSelector(
                EMBOSS_REQ_TYPE_SELECT_CSS_SELECTOR + " option[value='" + ON_REQ_OPTION + "']")).click();

        driver.findElement(cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndGetElementByLocator(id("lblVerifySessionID")).getText());
        driver.waitForAndGetElementByLocator(cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testPrintPassage() {
        navigator.selectOptionFromItemMenu("printPassage");

        assertTrue(navigator.isDialogShown());
    }
}
