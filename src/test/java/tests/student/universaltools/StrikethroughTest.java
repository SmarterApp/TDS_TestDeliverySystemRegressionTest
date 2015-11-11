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
import static org.openqa.selenium.By.*;

/**
 * Created by emunoz on 11/2/15.
 */
public class StrikethroughTest extends SeleniumBaseTest {
    private static final String STRIKETHROUGH_ITEM_MENU_CLASS = "strikethrough";
    private static final String MC_ITEM_ID = "Item_2"; //The multiple choice item for the assessment

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");
        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 11
        driver.findElement(cssSelector("option[value=\"11\"]")).click();
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
    public void testStrikethroughItemMenu() {
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".optionB .striked")));

        //Not in "strikethrough" mode yet
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#" + MC_ITEM_ID + ".mode-strikethrough")));

        navigator.selectOptionFromItemMenu(STRIKETHROUGH_ITEM_MENU_CLASS, 2);
        //Ensure we are in "strikethrough mode"
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#" + MC_ITEM_ID + ".mode-strikethrough")));
        //Strike B
        driver.findElement(By.cssSelector("#" + MC_ITEM_ID + " .optionB")).click();
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".optionB.strikethrough .striked")));
        //Verify that the opacity of the striked item is set to .5
        assertEquals(driver.findElement(By.cssSelector(".optionB .optionContent")).getCssValue("opacity"), "0.5");
        //Strike C
        driver.findElement(By.cssSelector("#" + MC_ITEM_ID + " .optionC")).click();
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".optionC.strikethrough .striked")));
        //Verify that the opacity of the striked item is set to .5
        assertEquals(driver.findElement(By.cssSelector(".optionC .optionContent")).getCssValue("opacity"), "0.5");

        //Make sure we didn't strike A
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".optionA.strikethrough .striked")));

        //Verify that the opacity of the unstriked item is set to 1
        assertEquals(driver.findElement(By.cssSelector(".optionA .optionContent")).getCssValue("opacity"), "1");

        assertTrue(driver.findElement(By.cssSelector(".optionB")).getAttribute("class")
                .contains(STRIKETHROUGH_ITEM_MENU_CLASS));
        assertTrue(driver.findElement(By.cssSelector(".optionC")).getAttribute("class")
                .contains(STRIKETHROUGH_ITEM_MENU_CLASS));

        //Unstrike C
        driver.findElement(By.cssSelector("#" + MC_ITEM_ID + " .optionC")).click();
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".optionC.strikethrough .striked")));
        assertEquals(driver.findElement(By.cssSelector(".optionC .optionContent")).getCssValue("opacity"), "1");
    }

    @Test
    public void testStrikethroughContextMenu() {
        WebElement optionB = driver.findElement(By.cssSelector("#" + MC_ITEM_ID + " .optionB"));
        WebElement optionC = driver.findElement(By.cssSelector("#" + MC_ITEM_ID + " .optionC"));
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".optionB .striked")));

        //Strike B
        toggleStrikethroughOnElement(optionB);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".optionB.strikethrough .striked")));
        //Verify that the opacity of the striked item is set to .5
        assertEquals(driver.findElement(By.cssSelector(".optionB .optionContent")).getCssValue("opacity"), "0.5");
        //Strike C
        toggleStrikethroughOnElement(optionC);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".optionC.strikethrough .striked")));
        //Verify that the opacity of the striked item is set to .5
        assertEquals(driver.findElement(By.cssSelector(".optionC .optionContent")).getCssValue("opacity"), "0.5");

        //Make sure we didn't strike A
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".optionA.strikethrough .striked")));

        //Verify that the opacity of the unstriked item is set to 1
        assertEquals(driver.findElement(By.cssSelector(".optionA .optionContent")).getCssValue("opacity"), "1");

        assertTrue(driver.findElement(By.cssSelector(".optionB")).getAttribute("class")
                .contains(STRIKETHROUGH_ITEM_MENU_CLASS));
        assertTrue(driver.findElement(By.cssSelector(".optionC")).getAttribute("class")
                .contains(STRIKETHROUGH_ITEM_MENU_CLASS));

        //Unstrike C
        toggleStrikethroughOnElement(optionC);
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".optionC.strikethrough .striked")));
        assertEquals(driver.findElement(By.cssSelector(".optionC .optionContent")).getCssValue("opacity"), "1");
    }

    /**
     * Moves to the specified option {@link WebElement}, right clicks, and then selects the "strikethrough" option.
     *
     * @param option
     */
    private void toggleStrikethroughOnElement(WebElement option) {
        Actions builder = new Actions(driver);
        builder.moveToElement(option, 10, 10)
                .contextClick()
                .build()
                .perform();
        driver.findElement(By.cssSelector(".yuimenu.visible ." + STRIKETHROUGH_ITEM_MENU_CLASS)).click();
    }
}
