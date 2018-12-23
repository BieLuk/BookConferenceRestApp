package bepoland.bookconference.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDTO {

    private UserBookingDTO user;
    private RoomBookingDTO room;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Warsaw")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Warsaw")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTo;
}
