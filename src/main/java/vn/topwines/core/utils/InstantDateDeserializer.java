package vn.topwines.core.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class InstantDateDeserializer extends JsonDeserializer<Instant> {
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneOffset.UTC);

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        LocalDate localDate = LocalDate.parse(p.getValueAsString(), fmt);
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    }
}