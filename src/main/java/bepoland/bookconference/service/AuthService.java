package bepoland.bookconference.service;

import bepoland.bookconference.Config.Security.JwtTokenProvider;
import bepoland.bookconference.Exception.AppException;
import bepoland.bookconference.dto.UserLoginDTO;
import bepoland.bookconference.dto.UserSignUpDTO;
import bepoland.bookconference.model.Role;
import bepoland.bookconference.model.RoleName;
import bepoland.bookconference.model.User;
import bepoland.bookconference.repository.RoleRepository;
import bepoland.bookconference.repository.UserRepository;
import bepoland.bookconference.response.ApiResponse;
import bepoland.bookconference.response.JwtAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    final AuthenticationManager authenticationManager;
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final PasswordEncoder passwordEncoder;
    final JwtTokenProvider tokenProvider;
    private ModelMapper modelMapper = new ModelMapper();


    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(UserLoginDTO userLoginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getLogin(),
                        userLoginDTO.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
//        User user =
                userRepository.findByLogin(userLoginDTO.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist."));
//        UserSimpleDTO userDTO = modelMapper.map(user, UserSimpleDTO.class);
//        return new JwtAuthenticationResponse(jwt);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    public ApiResponse registerUser(UserSignUpDTO userSignUpDTO) {
        if(userRepository.existsByLogin(userSignUpDTO.getLogin())) {
            return new ApiResponse(false, "Login is already taken.");
        }

        // Creating user's account
        User user = new User(userSignUpDTO.getName(), userSignUpDTO.getSurname(),
                userSignUpDTO.getLogin(), userSignUpDTO.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);
        return new ApiResponse(true, "User created successfully.");
    }




}
