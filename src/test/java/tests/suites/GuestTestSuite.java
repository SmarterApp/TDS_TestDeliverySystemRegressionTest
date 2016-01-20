package tests.suites;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.categories.AccessibilityTest;
import tests.categories.DesignatedSupportsTest;
import tests.categories.StudentGuestTest;
import tests.categories.UniversalToolsTest;
import tests.student.accomodations.BrailleTest;
import tests.student.general.FullEndTest;
import tests.student.general.HelpTest;
import tests.student.general.LoginTest;
import tests.student.general.TutorialTest;
import tests.student.universaltools.ZoomTest;

/**
 * Created by emunoz on 1/20/16.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(StudentGuestTest.class)
@Categories.ExcludeCategory({
                            AccessibilityTest.class,
                            DesignatedSupportsTest.class,
                            UniversalToolsTest.class
})
@Suite.SuiteClasses({
        ZoomTest.class,
        FullEndTest.class,
        HelpTest.class,
        LoginTest.class
})
public class GuestTestSuite {
}
