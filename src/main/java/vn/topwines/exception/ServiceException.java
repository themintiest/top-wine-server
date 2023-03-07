package vn.topwines.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ServiceException extends RuntimeException {
    private final Map<String, Object> data;
    private final ServiceErrorCode errorCode;
    private final String ordinal;
    private final Response.Status status;

    public ServiceException(ServiceErrorCode errorCode, String ordinal, Response.Status status, Throwable cause) {
        super(cause);
        this.data = new HashMap<>();
        this.errorCode = errorCode;
        this.ordinal = ordinal;
        this.status = status;
    }

    public ServiceException(ServiceErrorCode errorCode, String ordinal, Response.Status status) {
        this(errorCode, ordinal, status, null);
    }

    public ServiceException with(String key, Object value) {
        getData().put(key, value);
        return this;
    }
}