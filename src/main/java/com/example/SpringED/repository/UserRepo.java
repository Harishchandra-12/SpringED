package com.example.SpringED.repository;

import com.example.SpringED.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User , ObjectId> {

    public Optional<User> findByUserName(String user);
}
