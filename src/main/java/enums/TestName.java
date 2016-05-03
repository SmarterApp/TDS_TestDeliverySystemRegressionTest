package enums;

/**
 * This enumeration defines assessment test names.
 *
 * Created by emunoz on 1/20/16.
 */
public enum TestName {
    GRADE_3_MATH("Grade 3 MATH"),
    GRADE_3_ELA("Grade 3 ELA"),
    PERF_GRADE_3_ELA("Perf Grade 3 ELA"),
    PERF_GRADE_3_MATH("Perf Grade 3 MATH"),
    IRP_PERF_GRADE_3_MATH("IRP Perf Grade 3 MATH"),
    IRP_GRADE_3_ELA("IRP Grade 3 ELA"),
    GRADES_3_TO_5_ELA("Grades 3 - 5 ELA"),
    GRADES_3_TO_5_MATH("Grades 3 - 5 MATH"),
    GRADE_6_MATH("Grade 6 MATH"),
    GRADES_6_TO_8_ELA("Grades 6 - 8 ELA"),
    GRADE_7_ELA("Grade 7 ELA"),
    GRADE_7_MATH("Grade 7 MATH"),
    GRADES_7_TO_8_MATH("Grades 7 - 8 MATH"),
    PERF_GRADE_11_ELA("Perf Grade 11 ELA"),
    HIGH_SCHOOL_ELA("High School ELA"),
    HIGH_SCHOOL_MATH("High School MATH");

    private TestName(String key) {
        this.key = key;
    }

    private String key;

    public String getKey() {
        return this.key;
    }
}
