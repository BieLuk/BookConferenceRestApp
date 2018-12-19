package bepoland.bookconference.controller;

import bepoland.bookconference.dto.UserLoginDTO;
import bepoland.bookconference.dto.UserSignUpDTO;
import bepoland.bookconference.response.ApiResponse;
import bepoland.bookconference.response.JwtAuthenticationResponse;
import bepoland.bookconference.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        return authService.authenticateUser(userLoginDTO);
    }

    @PostMapping("/signup")
    public ApiResponse registerUser(@Valid @RequestBody UserSignUpDTO userSignUpDTO) {
        return authService.registerUser(userSignUpDTO);
    }
}