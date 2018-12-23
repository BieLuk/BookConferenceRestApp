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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    @Value("${app.adminPass}")
    private String adminPass;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public ApiResponse createUser(@Valid UserCreateDTO userCreateDTO, String passKey) {
        if(passKey!=null && passKey.equals(adminPass)) {
            if (userRepository.existsByLogin(userCreateDTO.getLogin())) {
                return new ApiResponse(false, "Login is already taken.");
            }

            User user = modelMapper.map(userCreateDTO, User.class);
            user.setPassword(hashPassword(userCreateDTO.getPassword()));
            userRepository.save(user);
            return new ApiResponse(true, "User created successfully.");
        }
        return new ApiResponse(false, "Wrong password key.");
    }

    public ApiResponse editUser(@Valid UserEditDTO userEditDTO, String passKey) {
        if(passKey!=null && passKey.equals(adminPass)) {
            User user = userRepository.findByLogin(userEditDTO.getLogin())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found login: " + userEditDTO.getLogin()));
            if ((userEditDTO.getName() != null)) { user.setName(userEditDTO.getName()); }
            if ((userEditDTO.getSurname() != null)) { user.setSurname(userEditDTO.getSurname()); }
            if ((userEditDTO.getPassword() != null)) { user.setPassword(hashPassword(user.getPassword())); }

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

    public User authenticateUser(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found login: " + login));
        if(user.getPassword().equals(hashPassword(password))) {
            return user;
        } else {
            return null;
        }
    }

    private String hashPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();

            return String.format("%064x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
