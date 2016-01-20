package enums;

/**
 * This enumeration defines assessment test names.
 *
 * Created by emunoz on 1/20/16.
 */
public enum TestName {
    GRADES_3_TO_5_ELA("Grades 3 - 5 ELA"),

    GRADES_3_TO_5_MATH("Grades 3 - 5 MATH"),

    GRADE_3_MATH("Grade 3 MATH"),

    GRADE_3_ELA("Grade 3 ELA");

    private TestName(String key) {
        this.key = key;
    }

    private String key;

    public String getKey() {
        return this.key;
    }
}
