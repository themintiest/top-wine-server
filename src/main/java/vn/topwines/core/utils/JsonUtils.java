package vn.topwines.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import jakarta.ws.rs.BadRequestException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new BadRequestException("Error when parsing json to object");
        }
    }
}
