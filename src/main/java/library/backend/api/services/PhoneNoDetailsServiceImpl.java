package library.backend.api.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import library.backend.api.models.User;
import library.backend.api.repositories.UserRepository;

@Service
public class PhoneNoDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public PhoneNoDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNo) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNo(phoneNo)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist."));

        return user;
    }

}
