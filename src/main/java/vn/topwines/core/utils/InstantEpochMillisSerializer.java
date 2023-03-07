package vn.topwines.core.utils;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;

public class InstantEpochMillisSerializer extends JsonSerializer<Instant> {

    @Override
    public void serialize(Instant value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.toEpochMilli());
    }
}
