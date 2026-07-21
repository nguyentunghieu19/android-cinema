package com.cinema.backend.config;

import com.cinema.backend.entity.Role;
import com.cinema.backend.entity.User;
import com.cinema.backend.repository.RoleRepository;
import com.cinema.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role ADMIN"));

        if (userRepository.findByUsername("admin").isEmpty()) {

            User admin = new User();

            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@gmail.com");
            admin.setFullname("Administrator");
            admin.setStatus(true);
            admin.setRole(adminRole);

            userRepository.save(admin);

            System.out.println("===== ADMIN CREATED =====");
        }
    }
}