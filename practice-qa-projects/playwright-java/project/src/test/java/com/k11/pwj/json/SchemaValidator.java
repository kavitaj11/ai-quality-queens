package com.k11.pwj.json;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Validates JSON against schema files in src/test/resources/schemas. A schema proves the
 * STRUCTURE of a response (fields, types, formats); tests still assert the business values.
 */
public final class SchemaValidator {

    private static final JsonSchemaFactory FACTORY = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    private static final Map<String, JsonSchema> CACHE = new ConcurrentHashMap<>();

    private SchemaValidator() {
    }

    /** Throws an AssertionError listing every violation, or returns quietly if the JSON is valid. */
    public static void assertMatches(String schemaName, String json) {
        Set<ValidationMessage> errors = schema(schemaName).validate(Json.tree(json));
        if (!errors.isEmpty()) {
            throw new AssertionError("Response does not match schema '" + schemaName + "':\n  "
                    + errors.stream().map(ValidationMessage::getMessage).sorted().collect(Collectors.joining("\n  "))
                    + "\nBody: " + json);
        }
    }

    private static JsonSchema schema(String name) {
        return CACHE.computeIfAbsent(name, n -> {
            InputStream in = SchemaValidator.class.getResourceAsStream("/schemas/" + n + ".schema.json");
            if (in == null) {
                throw new IllegalArgumentException("No schema file: schemas/" + n + ".schema.json");
            }
            return FACTORY.getSchema(in);
        });
    }
}
