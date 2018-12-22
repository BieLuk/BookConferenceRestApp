package bepoland.bookconference.service;

import bepoland.bookconference.dto.UserDTO;
import bepoland.bookconference.dto.UserEditDTO;
import bepoland.bookconference.dto.UserCreateDTO;
import bepoland.bookconference.model.User;
import bepoland.bookconference.repository.UserRepository;
import bepoland.bookconference.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${app.adminPass}")
    private String adminPass;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserDTO getUser(String userLogin) {
        User user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new ResourceNotFoundException("User not found login: " + userLogin));
        return modelMapper.map(user, UserDTO.class);
    }

    public ApiResponse createUser(UserCreateDTO userCreateDTO, String passKey) {
        if(passKey!=null && passKey.equals(adminPass)) {
            if (userRepository.existsByLogin(userCreateDTO.getLogin())) {
                return new ApiResponse(false, "Login is already taken.");
            }

            User user = modelMapper.map(userCreateDTO, User.class);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        return userRepository.findAll().stream()
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
