package tests.student.practicetest.universaltools;

import driver.BrowserInteractionType;
import enums.TestButton;
import enums.TestName;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import tests.StudentPracticeTestBaseTest;
import tests.categories.StudentGuestTest;

import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 11/2/15.
 */
@Category(StudentGuestTest.class)
public class ZoomTest extends StudentPracticeTestBaseTest {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###.##");
    private static final String ZOOM_LEVEL_0 = "TDS_PS_L0";     //Default, no zoom
    private static final String ZOOM_LEVEL_1 = "TDS_PS_L1";
    private static final String ZOOM_LEVEL_2 = "TDS_PS_L2";
    private static final String ZOOM_LEVEL_3 = "TDS_PS_L3";
    private static final String ZOOM_LEVEL_4 = "TDS_PS_L4";     //Max zoom
    // The font-size of a "p" element rounded to 2 decimal places
    private static final Double ZOOM_0_PX = 17.03;
    //(16 [original font size] * 1.04 [.theQuestions em mod] * 2 [.bigTable em mod] * .9 [.bigTable table em mod] ) * .76 [% factor]
    private static final Double ZOOM_1_PX = 21.9;
    private static final Double ZOOM_2_PX = 27.38;
    private static final Double ZOOM_3_PX = 36.13;
    private static final Double ZOOM_4_PX = 45.98;


    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 11
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        navigator.selectTest(TestName.GRADE_3_ELA, BrowserInteractionType.MOUSE);
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testZoom() {
        //Ensure we are at default ZOOM level (0)
        assertTrue(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains(ZOOM_LEVEL_0));
        assertEquals(ZOOM_0_PX, getRoundedFontSize());

        //Click zoom out button - nothing should change as we are already at level 0.
        navigator.clickButton(TestButton.ZOOM_OUT);
        //Ensure we are at default ZOOM level (0)
        assertTrue(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains(ZOOM_LEVEL_0));
        assertEquals(ZOOM_0_PX, getRoundedFontSize());

        //Zoom to level 1
        navigator.clickButton(TestButton.ZOOM_IN);
        assertTrue(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains(ZOOM_LEVEL_1));
        assertEquals(ZOOM_1_PX, getRoundedFontSize());

        //Zoom to level 2
        navigator.clickButton(TestButton.ZOOM_IN);
        assertTrue(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains(ZOOM_LEVEL_2));
        assertEquals(ZOOM_2_PX, getRoundedFontSize());

        //Zoom to level 3
        navigator.clickButton(TestButton.ZOOM_IN);
        assertTrue(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains(ZOOM_LEVEL_3));
        assertEquals(ZOOM_3_PX, getRoundedFontSize());

        //Zoom to level 4
        navigator.clickButton(TestButton.ZOOM_IN);
        assertTrue(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains(ZOOM_LEVEL_4));
        assertEquals(ZOOM_4_PX, getRoundedFontSize());

        //Try to zoom again, but verify that level 4 is maintained.
        navigator.clickButton(TestButton.ZOOM_IN);
        assertTrue(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains(ZOOM_LEVEL_4));
        assertEquals(ZOOM_4_PX, getRoundedFontSize());

        //Zoom out to level 2 again
        navigator.clickButton(TestButton.ZOOM_OUT); //L3
        navigator.clickButton(TestButton.ZOOM_OUT); //L2
        assertTrue(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains(ZOOM_LEVEL_2));
        assertEquals(ZOOM_2_PX, getRoundedFontSize());

    }

    private Double getRoundedFontSize() {
        String fontSize = driver.findElement(By.cssSelector(".questionCell p")).getCssValue("font-size");
        String fontSizeRounded = decimalFormat.format(Double.parseDouble(fontSize.replace("px", "")));
        return Double.parseDouble(fontSizeRounded);
    }
}
