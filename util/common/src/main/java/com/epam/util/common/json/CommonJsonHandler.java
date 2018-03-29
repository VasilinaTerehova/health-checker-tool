package com.epam.util.common.json;

import com.epam.util.common.CommonUtilException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommonJsonHandler {
    private ObjectMapper objectMapper;

    private CommonJsonHandler() {
        this.objectMapper = new ObjectMapper();
    }

    public static CommonJsonHandler get() {
        return new CommonJsonHandler();
    }

    public <T> T getTypedValue( String jsonString, Class<T> valueType ) throws CommonUtilException {
        try {
            return objectMapper.readValue(jsonString, valueType);
        }
        catch ( IOException ex ){
            return null;
        }
    }

    public <T> T getTypedValueFromInnerField( String jsonString, Class<T> valueType, String... fieldNameChain ) throws CommonUtilException {
        try {
            JsonNode source = extractJsonNode( jsonString );
            for ( String fieldName : fieldNameChain ) {
                source = extractChildJsonNode( source, fieldName );
            }

            return source != null ? objectMapper.readValue(source.toString(), valueType) : null;
        }
        catch ( IOException ex ){
            throw new CommonUtilException( ex );
        }
    }

    public <T> List<T> getListTypedValueFromInnerField( String jsonString, Class<T> valueType, String... fieldNameChain ) throws CommonUtilException {
        try {
            List<T> result = new ArrayList<>();
            JsonNode source = extractJsonNode( jsonString );
            for ( String fieldName : fieldNameChain ) {
                source = extractChildJsonNode( source, fieldName );
            }

            if ( source != null && source.isArray() ) {
                Iterator iterator = source.elements();
                while ( iterator.hasNext() ) {
                    T typedValue = getTypedValue( iterator.next().toString(), valueType );
                    if ( typedValue != null ) {
                        result.add( typedValue );
                    }
                }
            }

            return result;
        }
        catch ( IOException ex ){
            throw new CommonUtilException( ex );
        }
    }

    private JsonNode extractJsonNode( String jsonString ) throws IOException {
        return objectMapper.readTree( jsonString );
    }

    private JsonNode extractChildJsonNode( JsonNode jsonNode, String fieldName ) throws IOException {
        if ( jsonNode != null && jsonNode.isArray() ) {
            Iterator<JsonNode> iterator = jsonNode.elements();
            while ( iterator.hasNext() ) {
                JsonNode childJsonNode = iterator.next();
                if ( childJsonNode.hasNonNull( fieldName ) ) {
                    return childJsonNode.get( fieldName );
                }
            }
        }

        return jsonNode != null ? jsonNode.get( fieldName ) : null;
    }
}
