package vn.topwines.exception;

import lombok.extern.jbosslog.JBossLog;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

//@Provider
@JBossLog
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    /**
     * Map an exception to a {@link Response}. Returning
     * {@code null} results in a {@link Response.Status#NO_CONTENT}
     * response. Throwing a runtime exception results in a
     * {@link Response.Status#INTERNAL_SERVER_ERROR} response.
     *
     * @param exception the exception to map to a response.
     * @return a response mapped from the supplied exception.
     */
    @Override
    public Response toResponse(BadRequestException exception) {
        log.error(exception.getMessage(), exception);
        Response.Status status = Response.Status.BAD_REQUEST;
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("code", status.getStatusCode())
                .add("message", exception.getMessage());
        return Response.status(status)
                .entity(builder.build())
                .build();
    }
}
