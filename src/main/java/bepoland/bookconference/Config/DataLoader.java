package bepoland.bookconference.Config;

import bepoland.bookconference.dto.UserSignUpDTO;
import bepoland.bookconference.model.Role;
import bepoland.bookconference.model.RoleName;
import bepoland.bookconference.model.User;
import bepoland.bookconference.repository.RoleRepository;
import bepoland.bookconference.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserService userService;
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
        userService.registerUser(modelMapper.map(user1, UserSignUpDTO.class));

        User user2 = new User();
        user2.setName("Jane");
        user2.setSurname("Doe");
        user2.setLogin("jdoe");
        user2.setPassword("mySecret");
        userService.registerUser(modelMapper.map(user2, UserSignUpDTO.class));

    }
}


