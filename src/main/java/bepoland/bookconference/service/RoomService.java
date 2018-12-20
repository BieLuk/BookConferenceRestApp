package bepoland.bookconference.service;

import bepoland.bookconference.Config.Security.JwtTokenProvider;
import bepoland.bookconference.Exception.AppException;
import bepoland.bookconference.dto.*;
import bepoland.bookconference.model.Role;
import bepoland.bookconference.model.RoleName;
import bepoland.bookconference.model.Room;
import bepoland.bookconference.model.User;
import bepoland.bookconference.repository.RoleRepository;
import bepoland.bookconference.repository.RoomRepository;
import bepoland.bookconference.repository.UserRepository;
import bepoland.bookconference.response.ApiResponse;
import bepoland.bookconference.response.JwtAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    @Value("${app.adminPass}")
    private String adminPass;

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public RoomDTO getRoom(String roomName) {
        Room room = roomRepository.findByName(roomName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found login: " + roomName));
        return modelMapper.map(room, RoomDTO.class);
    }
    // TODO passkey validation annotation

    public ApiResponse createRoom(RoomCreateDTO roomCreateDTO, String passKey) {
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

    public ApiResponse editRoom(RoomEditDTO roomEditDTO, String passKey) {
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
        return roomRepository.findAllByAvailable(true).stream()
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
