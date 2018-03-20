package com.epam.util.common.json;

import com.epam.util.common.CheckingParamsUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CommonJsonHandler {
    private Gson gson;

    private CommonJsonHandler() {
        this.gson = new Gson();
    }

    public static CommonJsonHandler get() {
        return new CommonJsonHandler();
    }

    public <T> T getTypedValue( String jsonString, String fieldName ) {
        Type type = new TypeToken<T>() {}.getType();

        return gson.fromJson( gson.fromJson(jsonString, JsonObject.class ).get( fieldName ), type);
    }

    public <T> List<T> getListTypedValue( String jsonString, String fieldName ) {
        Type type = new TypeToken<List<T>>() {}.getType();

        return gson.fromJson( getFieldObject( getJsonObject( jsonString ), fieldName ), type);
    }

    private JsonObject getJsonObject( String jsonString ) {
        return gson.fromJson(jsonString, JsonObject.class );
    }

    private JsonElement getFieldObject(JsonObject jsonObject, String fieldName ) {
        return jsonObject != null ? jsonObject.get( fieldName ) : null;
    }

}
