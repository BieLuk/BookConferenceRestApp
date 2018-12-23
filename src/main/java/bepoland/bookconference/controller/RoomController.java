package bepoland.bookconference.controller;

import bepoland.bookconference.dto.RoomCreateDTO;
import bepoland.bookconference.dto.RoomDTO;
import bepoland.bookconference.dto.RoomEditDTO;
import bepoland.bookconference.response.ApiResponse;
import bepoland.bookconference.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    final RoomService roomService;

    @PostMapping("/create")
    public ApiResponse createRoom(@RequestBody RoomCreateDTO roomCreateDTO, @RequestHeader(value="x-api-key") String passKey) {
        return roomService.createRoom(roomCreateDTO, passKey);
    }

    @PutMapping("/edit")
    public ApiResponse editRoom(@RequestBody RoomEditDTO roomEditDTO, @RequestHeader(value="x-api-key") String passKey) {
        return roomService.editRoom(roomEditDTO, passKey);
    }

    @GetMapping("/available")
    public List<RoomDTO> getAvailableUsers() {
        return roomService.getAvailableRooms();
    }

    @DeleteMapping("/delete")
    public ApiResponse deleteRoom(String roomName, @RequestHeader(value="x-api-key") String passKey) {
        return roomService.deleteRoom(roomName, passKey);
    }
}
