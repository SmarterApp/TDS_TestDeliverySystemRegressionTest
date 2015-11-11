package tests.student.universaltools;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.*;

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

        //Grade 12
        driver.findElement(cssSelector("option[value=\"12\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(xpath("//ul[@id='testSelections']/li[2]")).click();
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
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

    @Test
    public void testDragDialog() throws InterruptedException {
        driver.waitForTitle("Student: Test", true);
        //Open glossary term dialog
        driver.findElement(By.className(GLOSSARY_TERM_CLASS)).click();
        WebElement header = driver.findElement(By.cssSelector("#WordListTool h2.hd"));
        Point headerLocation = header.getLocation();
        driver.findElement(By.cssSelector("#WordListTool h2.hd"));

        Point moveTo = new Point(200, -100);
        Actions builder = new Actions(driver);
        builder.moveToElement(header, 10, 10)
                .clickAndHold()
                .moveByOffset(moveTo.getX(), moveTo.getY())
                .release()
                .build().perform();

        //Ensure that the dialog was moved
        assertEquals(header.getLocation(),
                new Point(headerLocation.getX() + moveTo.getX(), headerLocation.getY() + moveTo.getY()));
    }
}
