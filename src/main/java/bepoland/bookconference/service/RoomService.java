package bepoland.bookconference.service;

import bepoland.bookconference.dto.*;
import bepoland.bookconference.model.Room;
import bepoland.bookconference.repository.RoomRepository;
import bepoland.bookconference.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class RoomService {

    @Value("${app.adminPass}")
    private String adminPass;

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public ApiResponse createRoom(@Valid RoomCreateDTO roomCreateDTO, String passKey) {
        if(passKey!=null && passKey.equals(adminPass)) {
            if (roomRepository.existsByName(roomCreateDTO.getName())) {
                return new ApiResponse(false, "Name is already taken.");
            }

            Room room = modelMapper.map(roomCreateDTO, Room.class);
            roomRepository.save(room);
            return new ApiResponse(true, "Room created successfully.");
        }
        return new ApiResponse(false, "Wrong password key.");
    }

    public ApiResponse editRoom(@Valid RoomEditDTO roomEditDTO, String passKey) {
        if(passKey!=null && passKey.equals(adminPass)) {
            Room room = roomRepository.findByName(roomEditDTO.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found name: " + roomEditDTO.getName()));

            if ((roomEditDTO.getLocationDescription() != null)) { room.setLocationDescription(roomEditDTO.getLocationDescription()); }
            if ((roomEditDTO.getNumberOfSeats() != null)) { room.setNumberOfSeats(roomEditDTO.getNumberOfSeats()); }
            if ((roomEditDTO.getHasProjector() != null)) { room.setHasProjector(roomEditDTO.getHasProjector()); }
            if ((roomEditDTO.getPhone() != null)) { room.setPhone(roomEditDTO.getPhone()); }

            roomRepository.save(room);
            return new ApiResponse(true, "Room edited successfully.");
        }
        return new ApiResponse(false, "Wrong password key.");
    }

    public List<RoomDTO> getAvailableRooms() {
        return roomRepository.findAll().stream()
                .map(room -> modelMapper.map(room, RoomDTO.class)).collect(Collectors.toList());
    }

    public ApiResponse deleteRoom(String roomName, String passKey) {
        if(passKey!=null && passKey.equals(adminPass)) {
            Room room = roomRepository.findByName(roomName)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found name: " + roomName));
            roomRepository.delete(room);
            return new ApiResponse(true, "Room deleted successfully.");
        }
        return new ApiResponse(false, "Wrong password key.");
    }

}
