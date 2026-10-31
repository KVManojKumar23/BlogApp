package org.com.seeder;

import java.util.Set;

import org.com.model.Roles;
import org.com.model.User;
import org.com.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component 
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    private void seedUsers() {
        // Guard check: Avoid duplicate key violations or repeated insertions on restart
        if (userRepo.count() == 0) {

            // 1. Seed Admin User
            User admin = User.builder()
                    .name("Super Admin")
                    .email("admin@example.com")
                    .phoneNum("9876543210")
                    .address("HQ Building")
                    .pin("560001")
                    .district("Bangalore Urban")
                    .state("Karnataka")
                    .country("India")
                    .roles(Set.of(Roles.ROLE_ADMIN, Roles.ROLE_USER))
                    .password(passwordEncoder.encode("Admin@123")) // Always hash seeded passwords
                    .active(true)
                    .build();

            userRepo.save(admin);

            System.out.println(">>> Database seeded: Default Admin created successfully.");
        } else {
            System.out.println(">>> Database already contains data. Seeding skipped.");
        }
    }

}
