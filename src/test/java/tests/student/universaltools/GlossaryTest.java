package tests.student.universaltools;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 11/3/15.
 */
public class GlossaryTest extends SeleniumBaseTest {
    private static final String GLOSSARY_TERM_CLASS = "TDS_WORD_LIST";

    private static final String GLOSSARY_TERM_HOVER_CLASS = "TDS_WORD_LIST_HOVER";

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");
        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 11
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
    public void testEnglishGlossary() {
        WebElement glossaryTermEl = driver.findElement(By.className(GLOSSARY_TERM_CLASS));
        String term = glossaryTermEl.getText();

        //Test hover
        driver.hoverOver(glossaryTermEl);
        assertTrue(glossaryTermEl.getAttribute("class").contains(GLOSSARY_TERM_HOVER_CLASS));

        glossaryTermEl.click();
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#wordListPanel")));
        assertEquals(term, driver.findElement(By.cssSelector("#WordListTool h2.hd")).getText());
        assertEquals("Glossary", driver.findElement(By.cssSelector("#wordListPanel ul a[href*='word-list']")).getText());

        //Close dialog
        driver.findElement(By.cssSelector("#wordListPanel a.container-close")).click();
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#wordListPanel")));

    }
}
