package tests.student.practicetest.universaltools;

import driver.BrowserInteractionType;
import enums.TestName;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.StudentPracticeTestBaseTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

/**
 * Created by emunoz on 5/3/16.
 */
public class IllustrationGlossaryTest extends StudentPracticeTestBaseTest {
    private static final String IG_SELECT_CSS_SELECTOR =  "select[id*='illustrationglossary']";
    private static final String IG_ON_OPTION = "TDS_ILG1";
    private static final String ARROW_WORD_TEXT = "arrow";
    private static final String SPINNER_WORD_TEXT = "spinner";

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL);
        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 12
        driver.findElement(cssSelector("option[value=\"7\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.GRADE_7_MATH, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        //Enable illustration glossary
        driver.findElement(By.cssSelector(
                IG_SELECT_CSS_SELECTOR + " option[value='" + IG_ON_OPTION + "']")).click();
        driver.findElement(cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testIllustrationGlossary() throws Exception {
        navigator.navigateToPage(4);
        driver.findElement(By.cssSelector("a#word-list-5")).click();
        Thread.sleep(500);
        assertTrue(driver.isElementVisibleNow(By.id("wordListPanel")));
        assertEquals(driver.findElement(By.cssSelector("#wordListPanel .yui-nav a")).getText(), "Illustration");
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#wordListPanel .yui-content img")));
        driver.findElement(By.cssSelector("#wordListPanel a.container-close")).click();
        Thread.sleep(500);
        assertFalse(driver.isElementVisibleNow(By.id("wordListPanel")));
    }

    @Test
    public void testIllustrationAndEnglishGlossary() throws Exception {
        navigator.navigateToPage(4);
        driver.findElement(By.cssSelector("a#word-list-6")).click();
        Thread.sleep(500);
        assertTrue(driver.isElementVisibleNow(By.id("wordListPanel")));
        assertEquals(driver.findElement(By.cssSelector("#wordListPanel .yui-nav a[href='#word-list-0']")).getText(), "Glossary");
        assertEquals(driver.findElement(By.cssSelector("#wordListPanel .yui-nav a[href='#word-list-1']")).getText(), "Illustration");
        //Ensure image is not shown (first tab - text glossary)
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#wordListPanel .yui-content p[style='']")));
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#wordListPanel .yui-content img")));
        //Click second tab
        driver.findElement(By.cssSelector("a[href='#word-list-1'")).click();
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#wordListPanel .yui-content p[style='']")));
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#wordListPanel .yui-content img")));

        driver.findElement(By.cssSelector("#wordListPanel a.container-close")).click();
        Thread.sleep(500);
        assertFalse(driver.isElementVisibleNow(By.id("wordListPanel")));
    }

    @Test
    public void testIllustrationGlossaryDragAndDrop() throws Exception {
        navigator.navigateToPage(4);
        driver.findElement(By.cssSelector("a#word-list-6")).click();
        Thread.sleep(500);
        assertTrue(driver.isElementVisibleNow(By.id("wordListPanel")));
        driver.findElement(By.cssSelector("a[href='#word-list-1'")).click();

        WebElement header = driver.findElement(By.cssSelector(".yui-dialog #wordListPanel h2.hd"));
        Point headerLocation = header.getLocation();
        driver.findElement(By.cssSelector(".yui-dialog #wordListPanel h2.hd"));
        Point moveTo = new Point(200, -100);
        Actions builder = new Actions(driver);
        builder.moveToElement(header, 10, 10)
                .clickAndHold()
                .moveByOffset(moveTo.getX(), moveTo.getY())
                .release()
                .build().perform();
        Thread.sleep(2000);
        //Ensure that the dialog was moved
        assertEquals(header.getLocation(),
                new Point(headerLocation.getX() + moveTo.getX(), headerLocation.getY() + moveTo.getY()));

        driver.findElement(By.cssSelector("#wordListPanel a.container-close")).click();
        Thread.sleep(500);
        assertFalse(driver.isElementVisibleNow(By.id("wordListPanel")));
    }

    @Test
    public void testIllustrationGlossaryResize() throws Exception {
        navigator.navigateToPage(4);
        driver.findElement(By.cssSelector("a#word-list-6")).click();
        Thread.sleep(500);
        assertTrue(driver.isElementVisibleNow(By.id("wordListPanel")));
        driver.findElement(By.cssSelector("a[href='#word-list-1'")).click();

        Dimension originalSize = driver.findElement(By.id("wordListPanel")).getSize();

        WebElement resizeEl = driver.findElement(By.cssSelector(".yui-dialog #wordListPanel .yui-resize-handle"));

        driver.findElement(By.cssSelector(".yui-dialog #wordListPanel h2.hd"));
        Point moveTo = new Point(200, 300);
        Actions builder = new Actions(driver);
        builder.moveToElement(resizeEl, 5, 5)
                .clickAndHold()
                .moveByOffset(moveTo.getX(), moveTo.getY())
                .release()
                .build().perform();
        Thread.sleep(1000);
        //Ensure that the dialog was resized (larger)
        Dimension newSize =  driver.findElement(By.id("wordListPanel")).getSize();
        assertTrue(newSize.getHeight() > originalSize.getHeight());
        assertTrue(newSize.getWidth() > originalSize.getWidth());

        // Resize again, this time smaller
        moveTo = new Point(-150, -200);
        builder.moveToElement(resizeEl, 5, 5)
                .clickAndHold()
                .moveByOffset(moveTo.getX(), moveTo.getY())
                .release()
                .build().perform();
        Thread.sleep(1000);

        driver.findElement(By.cssSelector("#wordListPanel a.container-close")).click();

        Dimension newSizeReduced =  driver.findElement(By.id("wordListPanel")).getSize();
        assertTrue(newSize.getHeight() > newSizeReduced.getHeight());
        assertTrue(newSize.getWidth() > newSizeReduced.getWidth());
        Thread.sleep(500);
        assertFalse(driver.isElementVisibleNow(By.id("wordListPanel")));
    }
}
