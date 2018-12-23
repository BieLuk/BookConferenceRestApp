package bepoland.bookconference.controller;

import bepoland.bookconference.dto.BookingCreateDTO;
import bepoland.bookconference.dto.BookingDTO;
import bepoland.bookconference.dto.RoomBookingDTO;
import bepoland.bookconference.dto.UserBookingDTO;
import bepoland.bookconference.response.ApiResponse;
import bepoland.bookconference.service.BookingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sun.plugin2.util.PojoUtil.toJson;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(BookingController.class)
public class StandaloneBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Value("${app.adminPass}")
    private String adminPass;

    private RoomBookingDTO roomBookingDTO1 = new RoomBookingDTO("Large Room");
    private RoomBookingDTO roomBookingDTO2 = new RoomBookingDTO("Medium Room");

    private UserBookingDTO userBookingDTO1 = new UserBookingDTO("John", "Smith");
    private UserBookingDTO userBookingDTO2 = new UserBookingDTO("Jane", "Doe");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime dateFrom1 = LocalDateTime.parse("2018-12-23 16:30:00", formatter);
    private LocalDateTime dateTo1 = LocalDateTime.parse("2018-12-23 17:30:00", formatter);

    private LocalDateTime dateFrom2 = LocalDateTime.parse("2018-12-23 11:30:00", formatter);
    private LocalDateTime dateTo2 = LocalDateTime.parse("2018-12-23 12:30:00", formatter);

    private BookingDTO booking1 = new BookingDTO(userBookingDTO1, roomBookingDTO1, dateFrom1, dateTo1);
    private BookingDTO booking2 = new BookingDTO(userBookingDTO2, roomBookingDTO2, dateFrom2, dateTo2);


    @Test
    public void givenBookings_whenGetBookingsFromAllRooms_thenReturnJsonArray_allArguments() throws Exception {

        List<BookingDTO> oneBooking = Arrays.asList(booking1);

        given(bookingService.getBookingsFromAllRooms("2018-12-23 15:00:00", "2018-12-23 20:00:00")).willReturn(oneBooking);

        mockMvc.perform(get("/book/all")
                .param("dateFrom", "2018-12-23 15:00:00")
                .param("dateTo", "2018-12-23 20:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void givenBookings_whenGetBookingsFromAllRooms_thenReturnJsonArray_dateFromOnly() throws Exception {

        List<BookingDTO> allBookings = Arrays.asList(booking1, booking2);

        given(bookingService.getBookingsFromAllRooms("2018-12-23 10:00:00", "")).willReturn(allBookings);

        mockMvc.perform(get("/book/all")
                .param("dateFrom", "2018-12-23 10:00:00")
                .param("dateTo", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void givenBookings_whenGetBookingsFromAllRooms_thenReturnJsonArray_dateToOnly() throws Exception {

        List<BookingDTO> allBookings = Arrays.asList(booking1, booking2);

        given(bookingService.getBookingsFromAllRooms("", "2018-12-23 20:00:00")).willReturn(allBookings);

        mockMvc.perform(get("/book/all")
                .param("dateFrom", "")
                .param("dateTo", "2018-12-23 20:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void givenBookings_whenGetBookingsFromAllRooms_thenReturnJsonArray_noArguments() throws Exception {

        List<BookingDTO> allBokings = Arrays.asList(booking1, booking2);

        given(bookingService.getBookingsFromAllRooms("", "")).willReturn(allBokings);

        mockMvc.perform(get("/book/all")
                .param("dateFrom", "")
                .param("dateTo", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void givenBookings_whenGetBookingsFromRoom_thenReturnJsonArray_allArguments() throws Exception {

        List<BookingDTO> oneBooking = Arrays.asList(booking1);

        given(bookingService.getBookingsFromRoom("Large Room","2018-12-23 15:00:00", "2018-12-23 20:00:00")).willReturn(oneBooking);

        mockMvc.perform(get("/book/room")
                .param("roomName", "Large Room")
                .param("dateFrom", "2018-12-23 15:00:00")
                .param("dateTo", "2018-12-23 20:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void givenBookings_whenGetBookingsFromRoom_thenReturnJsonArray_dateFromOnly() throws Exception {

        List<BookingDTO> oneBooking = Arrays.asList(booking1);

        given(bookingService.getBookingsFromRoom("Large Room","2018-12-23 10:00:00", "")).willReturn(oneBooking);

        mockMvc.perform(get("/book/room")
                .param("roomName", "Large Room")
                .param("dateFrom", "2018-12-23 10:00:00")
                .param("dateTo", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void givenBookings_whenGetBookingsFromRoom_thenReturnJsonArray_dateToOnly() throws Exception {

        List<BookingDTO> oneBooking = Arrays.asList(booking1);

        given(bookingService.getBookingsFromRoom("Large Room","", "2018-12-23 20:00:00")).willReturn(oneBooking);

        mockMvc.perform(get("/book/room")
                .param("roomName", "Large Room")
                .param("dateFrom", "")
                .param("dateTo", "2018-12-23 20:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void givenBookings_whenGetBookingsFromRoom_thenReturnJsonArray_noArguments() throws Exception {

        List<BookingDTO> oneBooking = Arrays.asList(booking1);

        given(bookingService.getBookingsFromRoom("Large Room","", "")).willReturn(oneBooking);

        mockMvc.perform(get("/book/room")
                .param("roomName", "Large Room")
                .param("dateFrom", "")
                .param("dateTo", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void givenBookings_whenGetBookingsByUser_thenReturnJsonArray_allArguments() throws Exception {

        List<BookingDTO> oneBooking = Arrays.asList(booking1);

        given(bookingService.getBookingsByUser("John","2018-12-23 15:00:00", "2018-12-23 20:00:00")).willReturn(oneBooking);

        mockMvc.perform(get("/book/user")
                .param("userName", "John")
                .param("dateFrom", "2018-12-23 15:00:00")
                .param("dateTo", "2018-12-23 20:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void givenBookings_whenGetBookingsByUser_thenReturnJsonArray_dateFromOnly() throws Exception {

        List<BookingDTO> oneBooking = Arrays.asList(booking1);

        given(bookingService.getBookingsByUser("John","2018-12-23 10:00:00", "")).willReturn(oneBooking);

        mockMvc.perform(get("/book/user")
                .param("userName", "John")
                .param("dateFrom", "2018-12-23 10:00:00")
                .param("dateTo", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void givenBookings_whenGetBookingsByUser_thenReturnJsonArray_dateToOnly() throws Exception {

        List<BookingDTO> oneBooking = Arrays.asList(booking1);

        given(bookingService.getBookingsByUser("John","", "2018-12-23 20:00:00")).willReturn(oneBooking);

        mockMvc.perform(get("/book/user")
                .param("userName", "John")
                .param("dateFrom", "")
                .param("dateTo", "2018-12-23 20:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void givenBookings_whenGetBookingsByUser_thenReturnJsonArray_noArguments() throws Exception {

        List<BookingDTO> oneBooking = Arrays.asList(booking1);

        given(bookingService.getBookingsByUser("John","", "")).willReturn(oneBooking);

        mockMvc.perform(get("/book/user")
                .param("userName", "John")
                .param("dateFrom", "")
                .param("dateTo", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void givenBookingCreateDto_whenBookRoom_thenReturnApiResponse_success() throws Exception {
        BookingCreateDTO booking = new BookingCreateDTO("jsmith", "qwerty", "Large Room", dateFrom1, dateTo1);

        given(bookingService.bookRoom(booking)).willReturn(new ApiResponse(true, "Successfully booked room."));

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking.getDateFrom().toString())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully booked room."));
    }



}
