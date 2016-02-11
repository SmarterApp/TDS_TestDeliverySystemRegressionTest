package tests.student.practicetest.universaltools;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.StudentPracticeTestBaseTest;
import util.ItemHandler;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by emunoz on 11/6/15.
 */
public class KeyboardNavigationTest extends StudentPracticeTestBaseTest {

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 11
        driver.findElement(By.cssSelector("option[value=\"11\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
        driver.waitForTitle("Student: Test", true);
    }

    @Test
    public void testMenuShortcuts() {
        Actions builder = new Actions(driver);

        //Ensure we're on the first item
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".showing #Item_1")));

        //Open global menu
        builder.keyDown(Keys.CONTROL)
                .sendKeys("g").keyUp(Keys.CONTROL)
                .build().perform();

        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yuimenu.visible")));
        assertAllMenuItemsPresent();

        builder.sendKeys(Keys.DOWN)
                .build().perform();
        String selectedClass =
                driver.findElement(By.cssSelector(".yuimenu.visible .yuimenuitem-selected")).getAttribute("class");
        assertTrue(selectedClass.contains("next"));

        //Close global menu
        builder.keyDown(Keys.CONTROL)
                .sendKeys("g").keyUp(Keys.CONTROL)
                .build().perform();

        //Open item menu
        builder.keyDown(Keys.CONTROL)
                .sendKeys("m").keyUp(Keys.CONTROL)
                .build().perform();

        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yuimenu.visible")));

        builder.sendKeys(Keys.DOWN)
                .build().perform();

        selectedClass =
                driver.findElement(By.cssSelector(".yuimenu.visible .yuimenuitem-selected")).getAttribute("class");
        assertTrue(selectedClass.contains("comment"));
    }

    @Test
    public void testPageNavigation() throws InterruptedException {
        Actions builder = new Actions(driver);

        //Ensure we're on the first item
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".showing #Item_1")));

        //Answer the first question and then move on to the second page
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(4000);

        assertFalse(driver.isElementVisibleNow(By.cssSelector(".showing #Item_1")));
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".showing #Item_2")));

        //Navigate back using keyboard navigation
        builder.keyDown(Keys.CONTROL)
                .sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL)
                .build().perform();

        //Ensure we've navigated back to the first item
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".showing #Item_1")));
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".showing #Item_2")));

        //Navigate next using keyboard navigation
        builder.keyDown(Keys.CONTROL)
                .sendKeys(Keys.ARROW_RIGHT)
                .build().perform();

        assertFalse(driver.isElementVisibleNow(By.cssSelector(".showing #Item_1")));
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".showing #Item_2")));
    }

    private void assertAllMenuItemsPresent() {
        List<String> menuItemClassNames =
                getMenuItemClassNames(driver.findElements(By.cssSelector(".yuimenu.visible li.yuimenuitem")));
        List<String> buttonClassNames =
                getButtonClassNames(driver.findElements(By.cssSelector("nav#navigation li a")));
        for (String menuItemClassName : menuItemClassNames) {
            if ("help".equals(menuItemClassName)) {
                assertTrue(driver.isElementVisibleNow(By.cssSelector("a#btnHelp")));
            } else {
                assertTrue(buttonClassNames.contains(menuItemClassName));
            }
        }

    }

    private List<String> getButtonClassNames(List<WebElement> buttons) {
        List<String> menuItemClasses = new ArrayList<>();

        for (WebElement button : buttons) {
            menuItemClasses.add(button.getAttribute("class").split(" ")[0]);
        }

        return menuItemClasses;
    }

    private List<String> getMenuItemClassNames(List<WebElement> globalMenuItems) {
        List<String> menuItemClasses = new ArrayList<>();

        for (WebElement menuItem : globalMenuItems) {
            String classes = menuItem.getAttribute("class");
            menuItemClasses.add(classes.split(" ")[1]);
        }

        return menuItemClasses;
    }
}
