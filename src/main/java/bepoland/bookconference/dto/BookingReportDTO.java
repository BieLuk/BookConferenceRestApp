package bepoland.bookconference.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingReportDTO {

    private RoomBookingDTO room;
    private LocalDateTime bookingTime;
}
