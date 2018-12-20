package bepoland.bookconference.Config;

import bepoland.bookconference.dto.RoomCreateDTO;
import bepoland.bookconference.dto.UserSignUpDTO;
import bepoland.bookconference.model.Role;
import bepoland.bookconference.model.RoleName;
import bepoland.bookconference.model.Room;
import bepoland.bookconference.model.User;
import bepoland.bookconference.repository.RoleRepository;
import bepoland.bookconference.service.RoomService;
import bepoland.bookconference.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${app.adminPass}")
    private String adminPass;

    private final UserService userService;
    private final RoomService roomService;

    private ModelMapper modelMapper = new ModelMapper();

    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);
        roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setName(RoleName.ROLE_ADMIN);
        roleRepository.save(adminRole);

        User user1 = new User();
        user1.setName("John");
        user1.setSurname("Smith");
        user1.setLogin("jsmith");
        user1.setPassword("qwerty");
        userService.registerUser(modelMapper.map(user1, UserSignUpDTO.class), adminPass);

        User user2 = new User();
        user2.setName("Jane");
        user2.setSurname("Doe");
        user2.setLogin("jdoe");
        user2.setPassword("mySecret");
        userService.registerUser(modelMapper.map(user2, UserSignUpDTO.class), adminPass);


        Room room1 = new Room();
        room1.setName("Large Room");
        room1.setLocationDescription("1st floor");
        room1.setNumberOfSeats(10);
        room1.setHasProjector(true);
        room1.setPhone("22-22-22-22");
        roomService.createRoom(modelMapper.map(room1, RoomCreateDTO.class), adminPass);

        Room room2 = new Room();
        room2.setName("Medium Room");
        room2.setLocationDescription("1st floor");
        room2.setNumberOfSeats(6);
        room2.setHasProjector(true);
        room2.setPhone("");
        roomService.createRoom(modelMapper.map(room2, RoomCreateDTO.class), adminPass);

        Room room3 = new Room();
        room3.setName("Small Room");
        room3.setLocationDescription("2nd floor");
        room3.setNumberOfSeats(4);
        room3.setHasProjector(false);
        room3.setPhone("");
        roomService.createRoom(modelMapper.map(room3, RoomCreateDTO.class), adminPass);
    }
}


