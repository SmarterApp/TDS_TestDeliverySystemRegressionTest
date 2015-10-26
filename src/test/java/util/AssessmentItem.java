package util;

import enums.AssessmentItemType;

/**
 * This object encapsulates a test assessment item element
 *
 * Created by emunoz on 10/21/15.
 */
public class AssessmentItem  {
    /**
     * The assessment item type.
     */
    private AssessmentItemType type;

    /**
     * The HTML id attribute for the assessment item element.
     */
    private String id;

    public AssessmentItem(String itemClassName, String id) {
        this.id = id;
        this.type = AssessmentItemType.fromString(itemClassName);
    }

    /**
     * Sets the {@link AssessmentItem} ID.
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setType(AssessmentItemType type) {
        this.type = type;
    }

    public AssessmentItemType getType() {
        return type;
    }


}
