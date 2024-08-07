package com.tpd.twitch.user;

import com.tpd.twitch.db.UserRepository;
import com.tpd.twitch.db.entity.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    // What dependencies does the following API need?
    private final UserDetailsManager userDetailsManager;   // The dependencies needed for the following API are written in AppConfig.
    private final PasswordEncoder passwordEncoder;         // The dependencies needed for the following API are written in AppConfig.
    private final UserRepository userRepository;

    public UserService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public void register(String username, String password, String firstName, String lastName) {
        UserDetails user = User.builder()    // .builder() - builder pattern allows you to chain operations below to access the methods provided by the builder pattern. Optional operations can be omitted. If omitted, default settings will be used.
                .username(username)
                .password(passwordEncoder.encode(password))   // Note the pw is encrypted
                .roles("USER")
                .build();
        userDetailsManager.createUser(user);
        userRepository.updateNameByUsername(username, firstName, lastName);

        // The code used above this line is all default code from Spring Security. Spring Security doesn't concern
        // itself with user-related information that's unrelated to security.
        // We use the API to retrieve them for easier manipulation.
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
