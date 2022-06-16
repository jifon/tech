package com.med.tech.repository;

import com.med.tech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);


}

//Now we can use JpaRepository’s methods:
// save(), findOne(), findById(), findAll(),
// count(), delete(), deleteById()…
// without implementing these methods

