package tests.suites;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.categories.AccessibilityTest;
import tests.categories.DesignatedSupportsTest;
import tests.categories.StudentGuestTest;
import tests.categories.UniversalToolsTest;
import tests.student.practicetest.general.FullEndTest;
import tests.student.practicetest.general.HelpTest;
import tests.student.practicetest.general.LoginTest;
import tests.student.practicetest.universaltools.ZoomTest;

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
