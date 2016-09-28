package tds.base;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class used for debug to print JSON of given object
 */
public class ObjectToJson {
    private Object jsonObj;

    public ObjectToJson(Object jsonObj) {
        this.jsonObj = jsonObj;
    }

    public String getJsonFromObj() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;

        try {
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return jsonInString;
    }
}
