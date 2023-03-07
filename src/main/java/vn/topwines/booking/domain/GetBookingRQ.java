package vn.topwines.booking.domain;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.booking.constant.BookingStatus;
import vn.topwines.core.query.PagingRQ;

@Getter
@Setter
public class GetBookingRQ extends PagingRQ {
    private String code;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private BookingStatus bookingStatus;
}
