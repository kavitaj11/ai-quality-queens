package com.k11.pwj.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/** One configured ObjectMapper for the whole framework. */
public final class Json {

    /**
     * USE_BIG_DECIMAL_FOR_FLOATS: JSON numbers like 34.99 become BigDecimal, never double.
     * FAIL_ON_UNKNOWN_PROPERTIES: a new field in a response fails mapping, so contract changes
     * are noticed. Turn it off where the API is allowed to add fields.
     */
    public static final ObjectMapper MAPPER = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private Json() {
    }

    public static <T> T read(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new AssertionError("Response did not map to " + type.getSimpleName() + ": " + e.getOriginalMessage()
                    + "\nBody: " + json, e);
        }
    }

    public static JsonNode tree(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new AssertionError("Not valid JSON: " + json, e);
        }
    }
}
