package bepoland.bookconference.controller;

import bepoland.bookconference.dto.UserDTO;
import bepoland.bookconference.dto.UserEditDTO;
import bepoland.bookconference.dto.UserLoginDTO;
import bepoland.bookconference.dto.UserSignUpDTO;
import bepoland.bookconference.response.ApiResponse;
import bepoland.bookconference.response.JwtAuthenticationResponse;
import bepoland.bookconference.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        return userService.authenticateUser(userLoginDTO);
    }

    @PostMapping("/signup")
    public ApiResponse registerUser(@Valid @RequestBody UserSignUpDTO userSignUpDTO, @RequestHeader(value="x-api-key") String passKey) {
        return userService.registerUser(userSignUpDTO, passKey);
    }

    @PutMapping("/edit")
    public ApiResponse editUser(@Valid @RequestBody UserEditDTO userEditDTO, @RequestHeader(value="x-api-key") String passKey) {
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