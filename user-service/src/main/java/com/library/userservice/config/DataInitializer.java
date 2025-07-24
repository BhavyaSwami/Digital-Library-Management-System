package com.library.userservice.config;

import com.library.userservice.model.User;
import com.library.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if we already have users
        if (userRepository.count() == 0) {
            // Create admin user
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@library.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setMembershipId("ADMIN001");
            admin.setRole("ADMIN");
            admin.setActive(true);
            userRepository.save(admin);

            // Create student user
            User student = new User();
            student.setName("Student User");
            student.setEmail("student@library.com");
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student"));
            student.setMembershipId("MEM123456");
            student.setRole("CUSTOMER");
            student.setActive(true);
            userRepository.save(student);

            System.out.println("Sample users created successfully!");
        }
    }
}