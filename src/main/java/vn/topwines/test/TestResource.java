package vn.topwines.test;

import lombok.RequiredArgsConstructor;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {

    @GET
    @PermitAll
    @Path("")
    public String test() {
        return "success";
    }
}
