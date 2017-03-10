package com.connectto.general.util;

//import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;

/**
 * Created by htdev001 on 2/6/15.
 */
public class JsonUtils {


    public static <C> List<C> castToListObject(String jsonArray, C aClas) {
        /*ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonArray, mapper.getTypeFactory().constructCollectionType(List.class, aClas.getClass()));
        } catch (IOException e) {
            return null;
        }*/
        return null;
    }
}
