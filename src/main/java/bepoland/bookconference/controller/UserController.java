package bepoland.bookconference.controller;

import bepoland.bookconference.dto.UserDTO;
import bepoland.bookconference.dto.UserEditDTO;
import bepoland.bookconference.dto.UserCreateDTO;
import bepoland.bookconference.response.ApiResponse;
import bepoland.bookconference.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @GetMapping
    public UserDTO getUser(String userLogin) {
        return userService.getUser(userLogin);
    }

    @PostMapping("/create")
    public ApiResponse registerUser(@RequestBody UserCreateDTO userCreateDTO, @RequestHeader(value="x-api-key") String passKey) {
        return userService.createUser(userCreateDTO, passKey);
    }

    @PutMapping("/edit")
    public ApiResponse editUser(@RequestBody UserEditDTO userEditDTO, @RequestHeader(value="x-api-key") String passKey) {
        return userService.editUser(userEditDTO, passKey);
    }

    @GetMapping("/available")
    public List<UserDTO> getAvailableUsers() {
        return userService.getAvailableUsers();
    }

    @DeleteMapping("/delete")
    public ApiResponse deleteUser(String userLogin, @RequestHeader(value="x-api-key") String passKey) {
        return userService.deleteUser(userLogin, passKey);
    }
}