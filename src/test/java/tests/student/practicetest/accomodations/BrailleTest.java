package tests.student.practicetest.accomodations;

import driver.BrowserInteractionType;
import enums.TestName;
import net.lightbody.bmp.core.har.*;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.StudentPracticeTestBaseTest;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

/**
 * Created by emunoz on 11/10/15.
 */
public class BrailleTest extends StudentPracticeTestBaseTest {
    private static final String LANGUAGE_SELECT_CSS_SELECTOR = "select[id*='language']";
    private static final String EMBOSS_REQ_TYPE_SELECT_CSS_SELECTOR = "select[id*='embossrequesttype']";
    private static final String PRINT_ON_REQ_SELECT_CSS_SELECTOR = "select[id*='printonrequest']";
    private static final String BRAILLE_LANGUAGE_OPTION = "ENU-Braille";
    private static final String BRAILLE_TYPE_SELECT_CSS_SELECTOR = "select[id*='brailletype']";
    private static final String BRAILLE_TYPE_CONTRACTED_OPTION = "TDS_BT_G2";
    private static final String ON_REQ_OPTION = "TDS_ERT_OR";
    private static final String PASSAGES_AND_ITEMS = "TDS_PoD_Stim&TDS_PoD_Item";
    private static final String PRINT_API_URL_FILTER = "/API/TestShell.axd/print";

    @Before
    public void loginAndBeginTest() throws Exception {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        driver.findElement(cssSelector("option[value=\"12\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select test type
        navigator.selectTest(TestName.HIGH_SCHOOL_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);

        //Enable Braille
        driver.findElement(cssSelector(
                LANGUAGE_SELECT_CSS_SELECTOR + " option[value='" + BRAILLE_LANGUAGE_OPTION + "']")).click();

        //Select contracted braille type
        driver.findElement(cssSelector(
                BRAILLE_TYPE_SELECT_CSS_SELECTOR + " option[value='" + BRAILLE_TYPE_CONTRACTED_OPTION + "']")).click();

        //Emboss option on-request
        driver.findElement(cssSelector(
                EMBOSS_REQ_TYPE_SELECT_CSS_SELECTOR + " option[value='" + ON_REQ_OPTION + "']")).click();

        //Print on request
        driver.findElement(cssSelector(
                PRINT_ON_REQ_SELECT_CSS_SELECTOR + " option[value='" + PASSAGES_AND_ITEMS + "']")).click();

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
    public void testPrintPassage() {
        proxy.newHar("BrailleTest");
        proxy.endHar();
        proxy.newHar("Test");
        Har har = proxy.getHar();
        HarLog log = har.getLog();
        driver.findElement(By.cssSelector("a.passageMenu")).click();
        driver.findElement(By.cssSelector(".yuimenu.visible .printPassage")).click();

        assertTrue(navigator.isDialogShown());
        navigator.clickDialogOkButton();
        assertFalse(navigator.isDialogShown());

        HarRequest printRequest = getRequestFromHarEntries(log.getEntries(), PRINT_API_URL_FILTER);
        assertNotNull(printRequest);
        assertEquals(printRequest.getMethod(), "POST");
        assertEquals("true", getQueryStringValueForName(printRequest.getQueryString(), "brailleEnabled"));

    }

    private String getQueryStringValueForName(List<HarNameValuePair> pairs, String name) {
        String value = null;

        for (HarNameValuePair qsPair : pairs) {
            if (name.equals(qsPair.getName())) {
                value = qsPair.getValue();
            }
        }

        return value;
    }

    private HarRequest getRequestFromHarEntries(List<HarEntry> entries, String filter) {
        HarRequest myRequest = null;

        // Start from end of array (most recent requests)
        for (int i = entries.size() - 1; i >= 0; --i) {
            HarEntry entry = entries.get(i);
            HarRequest request = entry.getRequest();
            if (request.getUrl().contains(filter)) {
                myRequest = request;
                break;
            }
        }

        return myRequest;
    }
}
