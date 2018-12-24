package bepoland.bookconference.service;

import bepoland.bookconference.config.ApiResponseExceptionHandler;
import bepoland.bookconference.dto.BookingCreateDTO;
import bepoland.bookconference.dto.BookingDTO;
import bepoland.bookconference.exception.ResourceNotFoundException;
import bepoland.bookconference.model.Booking;
import bepoland.bookconference.model.Room;
import bepoland.bookconference.model.User;
import bepoland.bookconference.repository.BookingRepository;
import bepoland.bookconference.repository.RoomRepository;
import bepoland.bookconference.repository.UserRepository;
import bepoland.bookconference.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class BookingService {

    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public List<BookingDTO> getBookingsFromAllRooms(String dateFrom, String dateTo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if(dateFrom == null && dateTo == null){
            return bookingRepository.findAll().stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        } else if(dateFrom != null && dateTo == null) {
            LocalDateTime dateFromFormatted = LocalDateTime.parse(dateFrom, formatter);
            return bookingRepository.findAllByDateFromGreaterThanEqual(dateFromFormatted).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        } else if(dateFrom == null && dateTo != null) {
            LocalDateTime dateToFormatted = LocalDateTime.parse(dateTo, formatter);
            return bookingRepository.findAllByDateToLessThanEqual(dateToFormatted).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        } else {
            LocalDateTime dateFromFormatted = LocalDateTime.parse(dateFrom, formatter);
            LocalDateTime dateToFormatted = LocalDateTime.parse(dateTo, formatter);
            return bookingRepository.findAllByDateFromGreaterThanEqualAndDateToLessThanEqual(dateFromFormatted, dateToFormatted).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        }
    }

    public List<BookingDTO> getBookingsFromRoom(String roomName, String dateFrom, String dateTo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Room room = roomRepository.findByName(roomName)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found name: " + roomName));
//                .orElseThrow(() -> new ApiResponseExceptionHandler().handleException(new ResourceNotFoundException("Room not found name: "), roomName));

        if(dateFrom == null && dateTo == null){
            return bookingRepository.findAllByRoom(room).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        } else if(dateFrom != null && dateTo == null) {
            LocalDateTime dateFromFormatted = LocalDateTime.parse(dateFrom, formatter);
            return bookingRepository.findAllByRoomAndDateFromGreaterThanEqual(room, dateFromFormatted).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        } else if(dateFrom == null && dateTo != null) {
            LocalDateTime dateToFormatted = LocalDateTime.parse(dateTo, formatter);
            return bookingRepository.findAllByRoomAndDateToLessThanEqual(room, dateToFormatted).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        } else {
            LocalDateTime dateFromFormatted = LocalDateTime.parse(dateFrom, formatter);
            LocalDateTime dateToFormatted = LocalDateTime.parse(dateTo, formatter);
            return bookingRepository.findAllByRoomAndDateFromGreaterThanEqualAndDateToLessThanEqual(room, dateFromFormatted, dateToFormatted).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        }
    }

    public List<BookingDTO> getBookingsByUser(String userName, String dateFrom, String dateTo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found name: " + userName));

        if(dateFrom == null && dateTo == null){
            return bookingRepository.findAllByUser(user).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        } else if(dateFrom != null && dateTo == null) {
            LocalDateTime dateFromFormatted = LocalDateTime.parse(dateFrom, formatter);
            return bookingRepository.findAllByUserAndDateFromGreaterThanEqual(user, dateFromFormatted).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        } else if(dateFrom == null && dateTo != null) {
            LocalDateTime dateToFormatted = LocalDateTime.parse(dateTo, formatter);
            return bookingRepository.findAllByUserAndDateToLessThanEqual(user, dateToFormatted).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        } else {
            LocalDateTime dateFromFormatted = LocalDateTime.parse(dateFrom, formatter);
            LocalDateTime dateToFormatted = LocalDateTime.parse(dateTo, formatter);
            return bookingRepository.findAllByUserAndDateFromGreaterThanEqualAndDateToLessThanEqual(user, dateFromFormatted, dateToFormatted).stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
        }
    }

    public ApiResponse bookRoom(@Valid BookingCreateDTO bookingCreateDTO) {
        User authUser = userService.authenticateUser(bookingCreateDTO.getLogin(), bookingCreateDTO.getPassword());
        if(authUser != null) {
            if(bookingCreateDTO.getDateFrom().isAfter(bookingCreateDTO.getDateTo())) {
                return new ApiResponse(false, "Date to must be later than date from.");
            }
            Room room = roomRepository.findByName(bookingCreateDTO.getRoom())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found name: " + bookingCreateDTO.getRoom()));
            boolean doesExist = bookingRepository.existsByRoomAndDateToGreaterThanEqualAndDateFromLessThanEqual(room, bookingCreateDTO.getDateFrom(), bookingCreateDTO.getDateTo());
            if(doesExist) {
                return new ApiResponse(false, "Room is already booked.");
            }
            Booking booking = new Booking();
            booking.setUser(authUser);
            booking.setRoom(room);
            booking.setDateFrom(bookingCreateDTO.getDateFrom());
            booking.setDateTo(bookingCreateDTO.getDateTo());

            bookingRepository.save(booking);
            return new ApiResponse(true, "Successfully booked room.");
        }

        return new ApiResponse(false, "Bad login credentials.");
    }

}
