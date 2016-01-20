package tests.student.universaltools;

import enums.TestButton;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.SeleniumBaseTest;
import tests.categories.UniversalToolsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 10/29/15.
 */
public class CalculatorTest extends SeleniumBaseTest {
    private static final String CALCULATOR_IFRAME_ID = "frame-tool-calculator-ScientificInv-GraphingInv-Regression";
    private static final String CALCULATOR_ON_OPTION = "TDS_CalcSciInv&TDS_CalcGraphingInv&TDS_CalcRegress";
    private static final String CALCULATOR_SCIENTIFIC_CLASSNAME = "TDS_CalcSciInv";

    @Before
    public void loginAndConfigureCalculator() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 3
        driver.findElement(By.cssSelector("option[value=\"11\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("select[id*='calculator'] option[value='" + CALCULATOR_ON_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
    }

    @Category(UniversalToolsTest.class)
    @Test
    public void testDragDialog() throws InterruptedException {
        driver.waitForTitle("Student: Test", true);
        navigator.clickButton(TestButton.CALCULATOR);
        WebElement header = driver.findElement(By.cssSelector(".tool-calculator h2.hd"));
        Point headerLocation = header.getLocation();
        driver.findElement(By.cssSelector(".tool-calculator h2.hd"));
        Point moveTo = new Point(200, -100);
        // This wait is necessary for the UI to update and for test to succeed in non-debug mode
        Thread.sleep(500);
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

    @Category(UniversalToolsTest.class)
    @Test
    public void testInverseRegressionCalculator() throws InterruptedException {
        navigator.clickButton(TestButton.CALCULATOR);
        driver.switchToIframe(By.cssSelector("#" + CALCULATOR_IFRAME_ID));
        //Switch to regression calculator
        driver.findElement(By.cssSelector("#Regression")).click();
        driver.findElement(By.cssSelector("#reg-X-1")).sendKeys("1");
        driver.findElement(By.cssSelector("#reg-X-2")).sendKeys("2");
        driver.findElement(By.cssSelector("#reg-X-3")).sendKeys("3");
        driver.findElement(By.cssSelector("#reg-X-4")).sendKeys("4");
        driver.findElement(By.cssSelector("#reg-X-5")).sendKeys("5");
        driver.findElement(By.cssSelector("#reg-Y1-1")).sendKeys("6");
        driver.findElement(By.cssSelector("#reg-Y1-2")).sendKeys("7");
        driver.findElement(By.cssSelector("#reg-Y1-3")).sendKeys("8");
        driver.findElement(By.cssSelector("#reg-Y1-4")).sendKeys("9");
        driver.findElement(By.cssSelector("#reg-Y1-5")).sendKeys("12");
        clickCalcButton("Linear");
        assertEquals("Y1=(1.4)x+(4.2)",
                driver.findElement(By.cssSelector("#calculatorwidget textarea#textinput")).getAttribute("value").trim());
    }

    @Category(UniversalToolsTest.class)
    @Test
    public void testGraphingCalculator() {
        navigator.clickButton(TestButton.CALCULATOR);
        driver.switchToIframe(By.cssSelector("#" + CALCULATOR_IFRAME_ID));
        //Switch to graphing calculator
        driver.findElement(By.cssSelector("#GraphingInv")).click();
        clickCalcButton("variable");
        clickCalcButton("plus");
        clickCalcButton("num1");
        assertEquals("x+1", driver.findElement(By.cssSelector("#equa1")).getAttribute("value"));
        driver.findElement(By.cssSelector("#tableview")).click();
        assertEquals("-4", driver.findElement(By.cssSelector("#datatable tr:nth-child(2) td:nth-child(1)")).getText());
        driver.findElement(By.cssSelector("#graphview")).click();
        assertTrue(driver.findElement(By.cssSelector("#canvasHolder #canvas")).isDisplayed());
        driver.switchOutOfIFrame();

        //Close calculator
        driver.findElement(By.cssSelector(".tool-calculator-container a.container-close")).click();
        assertFalse(driver.isElementVisibleNow(By.className("tool-calculator-container")));
    }

    @Category(UniversalToolsTest.class)
    @Test
    public void testScientificCalculator() {
        navigator.clickButton(TestButton.CALCULATOR);
        assertTrue(driver.isElementVisibleNow(By.className(CALCULATOR_SCIENTIFIC_CLASSNAME)));

        driver.switchToIframe(By.cssSelector("#" + CALCULATOR_IFRAME_ID));
        clickCalcButton("num2");
        clickCalcButton("plus");
        clickCalcButton("num2");
        clickCalcButton("equals");
        assertEquals("4", getCalculatorValue());
        clickCalcButton("multiply");
        clickCalcButton("num4");
        clickCalcButton("equals");
        assertEquals("16", getCalculatorValue());
        clickCalcButton("C");
        clickCalcButton("sin");
        clickCalcButton("num1");
        clickCalcButton("num8");
        clickCalcButton("num0");
        clickCalcButton("equals");
        assertEquals("0", getCalculatorValue());
        clickCalcButton("C");
        clickCalcButton("sqrt");
        clickCalcButton("num1");
        clickCalcButton("num6");
        clickCalcButton("dot");
        clickCalcButton("num0");
        clickCalcButton("equals");
        assertEquals("4", getCalculatorValue());
        clickCalcButton("C");
        driver.findElement(By.cssSelector("#radians")).click();
        clickCalcButton("cos");
        clickCalcButton("pi");
        clickCalcButton("equals");
        assertEquals("-1", getCalculatorValue());
        driver.switchOutOfIFrame();
        //Close calculator
        driver.findElement(By.cssSelector(".tool-calculator-container a.container-close")).click();
        assertFalse(driver.isElementVisibleNow(By.className("tool-calculator-container")));
    }

    private void clickCalcButton(String id) {
        driver.findElement(By.cssSelector(".calculatorWidget #" + id)).click();
    }

    private String getCalculatorValue() {
        return driver.findElement(By.cssSelector("#textinput")).getAttribute("value");
    }
}
