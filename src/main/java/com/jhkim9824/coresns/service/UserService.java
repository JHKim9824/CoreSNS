package com.jhkim9824.coresns.service;

import com.jhkim9824.coresns.dto.UserRegistrationDto;
import com.jhkim9824.coresns.entity.User;
import com.jhkim9824.coresns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    public User registerUser(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setNickname(registrationDto.getNickname());
        user.setName(registrationDto.getName());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setGender(registrationDto.getGender());
        user.setBirthdate(registrationDto.getBirthdate());
        user.setIntroduce(registrationDto.getIntroduce());

        return userRepository.save(user);
    }
}
