package bepoland.bookconference.controller;

import bepoland.bookconference.dto.BookingCreateDTO;
import bepoland.bookconference.dto.BookingDTO;
import bepoland.bookconference.response.ApiResponse;
import bepoland.bookconference.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/all")
    public List<BookingDTO> getBookingsFromAllRooms(String dateFrom, String dateTo) {
        return bookingService.getBookingsFromAllRooms(dateFrom, dateTo);
    }

    @GetMapping("/room")
    public List<BookingDTO> getBookingsByRoom(String roomName, String dateFrom, String dateTo) {
        return bookingService.getBookingsFromRoom(roomName, dateFrom, dateTo);
    }

    @GetMapping("/user")
    public List<BookingDTO> getBookingsByUser(String userName, String dateFrom, String dateTo) {
        return bookingService.getBookingsByUser(userName, dateFrom, dateTo);
    }

    @PostMapping
    public ApiResponse bookRoom(@RequestBody BookingCreateDTO bookingCreateDTO) {
        return bookingService.bookRoom(bookingCreateDTO);
    }

}
