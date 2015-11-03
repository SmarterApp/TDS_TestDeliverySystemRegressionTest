package tests.student.universaltools;

import enums.TestButton;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 10/29/15.
 */
public class CalculatorTest extends SeleniumBaseTest {
    private static final String CALCULATOR_IFRAME_ID = "frame-tool-calculator-ScientificInv-GraphingInv-Regression";

    private static final String CALCULATOR_ON_OPTION = "TDS_CalcSciInv&TDS_CalcGraphingInv&TDS_CalcRegress";

    private static final String CALCULATOR_SCIENTIFIC_CLASSNAME = "TDS_CalcSciInv";

    private static final String CALCULATOR_GRAPH_CLASSNAME = "TDS_CalcGraphingInv";

    private static final String CALCULATOR_REGRESSION_CLASSNAME = "TDS_CalcRegress";

    @Before
    public void loginAndConfigureCalculator() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 3
        driver.findElement(By.cssSelector("option[value=\"11\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("select[id*='calculator'] option[value='" + CALCULATOR_ON_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();
    }

    @Test
    public void testGraphingCalculator() {
        //Switch to graphing calculator
        driver.findElement(By.cssSelector("#GraphingInv")).click();

    }

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
    }

    private void clickCalcButton(String id) {
        driver.findElement(By.cssSelector(".calcButtons #" + id)).click();
    }

    private String getCalculatorValue() {
        return driver.findElement(By.cssSelector("#textinput")).getAttribute("value");
    }
}
