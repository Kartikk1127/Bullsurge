package com.Kartikey.bullsurge.backend.repository;

import com.Kartikey.bullsurge.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

        Optional<User> findByName(String username);
}
