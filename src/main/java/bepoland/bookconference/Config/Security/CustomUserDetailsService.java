package bepoland.bookconference.Config.Security;

import bepoland.bookconference.model.User;
import bepoland.bookconference.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + login)
                );

        return UserPrincipal.create(user);
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }
}