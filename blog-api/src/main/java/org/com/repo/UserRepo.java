package org.com.repo;

import java.util.Optional;

import org.com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepo extends JpaRepository<User, Integer> {

    public Optional<User> findByEmail(String email);
    public boolean existsByEmail(String email);

}
