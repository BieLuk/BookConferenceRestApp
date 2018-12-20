package bepoland.bookconference.service;

import bepoland.bookconference.Config.Security.JwtTokenProvider;
import bepoland.bookconference.Exception.AppException;
import bepoland.bookconference.dto.UserDTO;
import bepoland.bookconference.dto.UserEditDTO;
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
public class UserService {

    @Value("${app.adminPass}")
    private String adminPass;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserDTO getUser(String userLogin) {
        User user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new ResourceNotFoundException("User not found login: " + userLogin));
        return modelMapper.map(user, UserDTO.class);
    }

    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(UserLoginDTO userLoginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getLogin(),
                        userLoginDTO.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
                userRepository.findByLogin(userLoginDTO.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist."));
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    public ApiResponse registerUser(UserSignUpDTO userSignUpDTO, String passKey) {
        if(passKey!=null && passKey.equals(adminPass)) {
            if (userRepository.existsByLogin(userSignUpDTO.getLogin())) {
                return new ApiResponse(false, "Login is already taken.");
            }

            User user = new User(userSignUpDTO.getName(), userSignUpDTO.getSurname(),
                    userSignUpDTO.getLogin(), userSignUpDTO.getPassword());

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));

            user.setRoles(Collections.singleton(userRole));

            userRepository.save(user);
            return new ApiResponse(true, "User created successfully.");
        }
        return new ApiResponse(false, "Wrong password key.");
    }

    public ApiResponse editUser(UserEditDTO userEditDTO, String passKey) {
        if(passKey!=null && passKey.equals(adminPass)) {
            User user = userRepository.findByLogin(userEditDTO.getLogin())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found login: " + userEditDTO.getLogin()));
            if ((userEditDTO.getName() != null)) { user.setName(userEditDTO.getName()); }
            if ((userEditDTO.getSurname() != null)) { user.setSurname(userEditDTO.getSurname()); }
            if ((userEditDTO.getPassword() != null)) { user.setPassword(passwordEncoder.encode(user.getPassword())); }

            userRepository.save(user);
            return new ApiResponse(true, "User edited successfully.");
        }
        return new ApiResponse(false, "Wrong password key.");

    }

    public List<UserDTO> getAvailableUsers() {
        return userRepository.findAllByAvailable(true).stream()
                .map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
    }

    public ApiResponse deleteUser(String userLogin, String passKey) {
        if(passKey!=null && passKey.equals(adminPass)) {
            User user = userRepository.findByLogin(userLogin)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found login: " + userLogin));
            userRepository.delete(user);
            return new ApiResponse(true, "User deleted successfully.");
        }
        return new ApiResponse(false, "Wrong password key.");
    }






}
