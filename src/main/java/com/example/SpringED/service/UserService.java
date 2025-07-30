package com.example.SpringED.service;

import com.example.SpringED.entity.JournalEntry;
import com.example.SpringED.entity.User;
import com.example.SpringED.repository.JournalEntryRepo;
import com.example.SpringED.repository.UserRepo;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class UserService {

    private final UserRepo userRepo;
    private final JournalEntryRepo journalEntryRepo;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserService(UserRepo userRepo, JournalEntryRepo journalEntryRepo) {
        this.userRepo = userRepo;
        this.journalEntryRepo = journalEntryRepo;
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }


    public Optional<User> getUserByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add("USER");
        userRepo.save(user);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public boolean updateUser(String userName, User user) {
        Optional<User> optionalUser = userRepo.findByUserName(userName);
        if (optionalUser.isPresent()) {
            optionalUser.get().setUserName(!user.getUserName().isEmpty() ? user.getUserName() : userName);
            optionalUser.get().setPassword(!user.getPassword().isEmpty() ? passwordEncoder.encode(user.getPassword()) : optionalUser.get().getPassword());
            userRepo.save(optionalUser.get());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteUserByUserName(String userName) {
        Optional<User> optionalUser = userRepo.findByUserName(userName);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Step 2: Delete associated journal entries using Stream API
            Optional.ofNullable(user.getJournalEntries())
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(JournalEntry::getId)
                    .forEach(journalEntryRepo::deleteById);
            userRepo.deleteByUserName(userName);
            return true;
        }
        return false;
    }

    public User saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add("ADMIN");
        user.getRoles().add("USER");
        return userRepo.save(user);
    }
}
