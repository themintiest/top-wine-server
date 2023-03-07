package vn.topwines.booking.repository;

import vn.topwines.booking.entity.Booking;
import vn.topwines.core.repository.BaseRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class BookingRepository extends BaseRepository<Booking, Long> {
    public Optional<Booking> findByCode(String code) {
        return find("code", code).singleResultOptional();
    }
}
