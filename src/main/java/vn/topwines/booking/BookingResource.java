package vn.topwines.booking;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.topwines.booking.constant.BookingStatus;
import vn.topwines.booking.domain.BookingDetailRS;
import vn.topwines.booking.domain.BookingRS;
import vn.topwines.booking.domain.CreateBookingRQ;
import vn.topwines.booking.domain.GetBookingRQ;
import vn.topwines.core.constants.Roles;
import vn.topwines.core.query.Pageable;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/booking")
@Tag(name = "Booking")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookingResource {
    private final BookingService bookingService;

    @POST
    @Path("/book")
    @PermitAll
    @Operation(summary = "Create booking", description = "Create booking")
    public Response createBooking(@RequestBody @Valid CreateBookingRQ createBookingRQ) {
        BookingDetailRS bookingDetailRS = bookingService.createBooking(createBookingRQ);
        return Response.ok(bookingDetailRS).build();
    }

    @PUT
    @Path("/management/{id}/update-booking-status")
    @RolesAllowed(Roles.ADMIN)
    @Operation(summary = "Update booking status", description = "Update booking status")
    public Response updateStatus(@PathParam("id") Long id, @QueryParam("status") BookingStatus status) {
        BookingDetailRS bookingDetailRS = bookingService.updateBookingStatus(id, status);
        return Response.ok(bookingDetailRS).build();
    }

    @POST
    @Path("/management/get-bookings")
    @RolesAllowed(Roles.ADMIN)
    @Operation(summary = "Get bookings", description = "Get bookings")
    public Response getBookings(@RequestBody @Valid GetBookingRQ getBookingRQ) {
        Pageable<BookingRS> page = bookingService.getBookings(getBookingRQ);
        return Response.ok(page).build();
    }

    @GET
    @Path("/management/{id}")
    @RolesAllowed(Roles.ADMIN)
    @Operation(summary = "Get booking by id", description = "Get booking by id")
    public Response getBookingById(@PathParam("id") Long id) {
        return Response.ok(bookingService.getDetailById(id)).build();
    }

    @GET
    @Path("/get-by-code")
    @PermitAll
    @Operation(summary = "Get booking by code", description = "Get booking by code")
    public Response getBookingByCode(@QueryParam("code") String code) {
        return Response.ok(bookingService.getDetailByCode(code)).build();
    }
}
