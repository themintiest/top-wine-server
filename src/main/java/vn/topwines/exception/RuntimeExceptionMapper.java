package vn.topwines.exception;

import lombok.extern.jbosslog.JBossLog;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Provider
@JBossLog
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Map<Class<? extends RuntimeException>, Response.Status> STATUS_MAP = new ConcurrentHashMap<>() {{
        put(AuthException.class, Response.Status.UNAUTHORIZED);
        put(EntityNotFoundException.class, Response.Status.NOT_FOUND);
        put(BadRequestException.class, Response.Status.BAD_REQUEST);
        put(EntityExistsException.class, Response.Status.BAD_REQUEST);
        put(IllegalArgumentException.class, Response.Status.BAD_REQUEST);
    }};

    @Override
    public Response toResponse(RuntimeException e) {
        Response.Status status = STATUS_MAP.getOrDefault(e.getClass(), Response.Status.INTERNAL_SERVER_ERROR);
        if (status.equals(Response.Status.INTERNAL_SERVER_ERROR)) {
            log.error(e.getMessage(), e);
        }
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("code", status.getStatusCode())
                .add("message", Response.Status.INTERNAL_SERVER_ERROR.equals(status) ? "Lỗi hệ thống" : getExceptionMessage(e));
//        if (e instanceof ViolationException) {
//            ViolationException ex = (ViolationException) e;
//            builder.add("violations", Json.createArrayBuilder(ex.getViolations()));
//            builder.add("code", String.valueOf(ex.getError() != null ? ex.getError().getCode() : status.getStatusCode()));
//        }
        return Response.status(status)
                .entity(builder.build())
                .build();
    }

    private String getExceptionMessage(Exception e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        while (isBlank(message) && cause != null) {
            message = cause.getMessage();
            cause = cause.getCause();
        }
        return (message != null) ? message : "";
    }
}
