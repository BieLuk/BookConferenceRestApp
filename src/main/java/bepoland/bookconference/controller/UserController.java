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
    public ApiResponse registerUser(@Valid @RequestBody UserSignUpDTO userSignUpDTO) {
        return userService.registerUser(userSignUpDTO);
    }

    @PutMapping
    public ApiResponse editUser(@Valid @RequestBody UserEditDTO userEditDTO) {
        return userService.editUser(userEditDTO);
    }

    @GetMapping("/available")
    public List<UserDTO> getAvailableUsers() {
        return userService.getAvailableUsers();
    }

    @DeleteMapping
    public ApiResponse deleteUser(String userLogin) {
        return userService.deleteUser(userLogin);
    }
}