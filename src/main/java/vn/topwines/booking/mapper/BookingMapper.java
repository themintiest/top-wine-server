package vn.topwines.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.booking.domain.BookingDetailRS;
import vn.topwines.booking.domain.BookingRS;
import vn.topwines.booking.domain.CreateBookingRQ;
import vn.topwines.booking.entity.Booking;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        uses = { BookingProductMapper.class }
)
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    Booking mapCreateBookingRQ(CreateBookingRQ createBookingRQ);

    BookingRS mapBookingToBookingRS(Booking booking);

    BookingDetailRS mapBookingToBookingDetailRS(Booking booking);
}
