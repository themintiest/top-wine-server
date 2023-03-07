package vn.topwines.booking.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.booking.constant.BookingStatus;
import vn.topwines.booking.entity.Booking;
import vn.topwines.core.repository.specification.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingSpecifications {
    public static BookingSpecificationBuilder builder() {
        return new BookingSpecificationBuilder();
    }

    public static class BookingSpecificationBuilder {
        private String code;
        private String customerName;
        private String customerPhone;
        private String customerEmail;
        private BookingStatus bookingStatus;

        public BookingSpecificationBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public BookingSpecificationBuilder withCustomerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public BookingSpecificationBuilder withCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public BookingSpecificationBuilder withCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
            return this;
        }

        public BookingSpecificationBuilder withBookingStatus(BookingStatus bookingStatus) {
            this.bookingStatus = bookingStatus;
            return this;
        }

        public Specification<Booking> build() {
            Specification<Booking> specification = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
            if (StringUtils.isNotBlank(code)) {
                specification = specification.and((root, query, cb) -> cb.equal(root.get("code"), code));
            }
            if (StringUtils.isNotBlank(customerName)) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")), String.format("%%%s%%", customerName.toLowerCase())));
            }
            if (StringUtils.isNotBlank(customerEmail)) {
                specification = specification.and((root, query, cb) -> cb.equal(root.get("customerEmail"), customerEmail));
            }
            if (StringUtils.isNotBlank(customerPhone)) {
                specification = specification.and((root, query, cb) -> cb.equal(root.get("customerPhone"), customerPhone));
            }
            if (bookingStatus != null) {
                specification = specification.and((root, query, cb) -> cb.equal(root.get("bookingStatus"), bookingStatus));
            }
            return specification;
        }
    }
}
