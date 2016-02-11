package tests.student.practicetest.designatedsupports;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.StudentPracticeTestBaseTest;

import static org.junit.Assert.assertEquals;

/**
 * Created by emunoz on 11/4/15.
 */
public class TranslatedDirectionsTest extends StudentPracticeTestBaseTest {
    private static final String LANGUAGE_SELECT_CSS_SELECTOR =  "select[id*='language']";
    private static final String SPANISH_LANGUAGE_OPTION = "ESN";

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 12
        driver.findElement(By.cssSelector("option[value=\"12\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
    }

    @Test
    public void testSpanishInstructions() {
        final String SPANISH_INSTRUCTIONS_TITLE = "Student: Login Shell Instrucciones y ayuda para la prueba";
        //Set language to Spanish
        driver.findElement(By.cssSelector(
                LANGUAGE_SELECT_CSS_SELECTOR + " option[value='" + SPANISH_LANGUAGE_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();

        //Test verification screen
        assertTestVerificationTranslated();
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle(SPANISH_INSTRUCTIONS_TITLE, false);
        assertTestInstructionsTranslated();
    }

    private void assertTestVerificationTranslated() {
        //TODO: Should we possibly import these texts from a localized properties file?
        final String SPANISH_TEST_VERIFY_HEADER = "¿Es ésta su prueba?";
        final String SPANISH_TEST_VERIFY_SESSIONID_LABEL = "ID de sesión:";
        final String SPANISH_TEST_VERIFY_INSTRUCTION_TEXT = "¿La prueba que aparece arriba es la prueba que desea realizar? " +
                "Si es así, haga clic en [Sí, comenzar la prueba], de lo contrario, haga clic en [No].";

        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        assertEquals(SPANISH_TEST_VERIFY_HEADER,
                driver.findElement(By.cssSelector("#sectionTestVerify h1#sectionTestVerifyHeader")).getText());
        assertEquals(SPANISH_TEST_VERIFY_SESSIONID_LABEL,
                driver.findElement(By.cssSelector("#verifySessionID > span")).getText());
        assertEquals(SPANISH_TEST_VERIFY_INSTRUCTION_TEXT,
                driver.findElement(By.cssSelector("#sectionTestVerify .instructions")).getText());
    }

    private void assertTestInstructionsTranslated() {
        final String SPANISH_INSTRUCTIONS_HEADER = "Instrucciones y ayuda para la prueba";
        final String SPANISH_INSTRUCTIONS_TEXT = "Haga clic [?] para acceder a la Guía de ayuda en " +
                "cualquier momento durante la prueba. Desplácese hacia abajo para obtener información adicional.";
        final String SPANISH_INSTRUCTIONS_CONTENT_LINK_1 = "Descripción general del sitio de prueba estudiantil";
        final String SPANISH_INSTRUCTIONS_CONTENT_LINK_2 = "Reglas de la prueba";
        final String SPANISH_INSTRUCTIONS_CONTENT_LINK_3 = "Sobre impresión por demanda";

        assertEquals(SPANISH_INSTRUCTIONS_HEADER,
                driver.findElement(By.cssSelector("#sectionInstructions h1#sectionInstructionsHeader")).getText());
        assertEquals(SPANISH_INSTRUCTIONS_TEXT,
                driver.findElement(By.cssSelector("#sectionInstructions .instructions")).getText());

        driver.switchToIframe(By.cssSelector("iframe#helpFrame"));
        assertEquals(SPANISH_INSTRUCTIONS_CONTENT_LINK_1,
                driver.findElement(By.cssSelector("#helpContent a[href='#one']")).getText());
        assertEquals(SPANISH_INSTRUCTIONS_CONTENT_LINK_2,
                driver.findElement(By.cssSelector("#helpContent a[href='#two']")).getText());
        assertEquals(SPANISH_INSTRUCTIONS_CONTENT_LINK_3,
                driver.findElement(By.cssSelector("#helpContent a[href='#three']")).getText());
    }
}
