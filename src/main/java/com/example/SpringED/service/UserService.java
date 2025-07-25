package com.example.SpringED.service;

import com.example.SpringED.entity.JournalEntry;
import com.example.SpringED.entity.User;
import com.example.SpringED.repository.JournalEntryRepo;
import com.example.SpringED.repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserRepo userRepo;
    private final JournalEntryRepo journalEntryRepo;


    public UserService(UserRepo userRepo, JournalEntryRepo journalEntryRepo) {
        this.userRepo = userRepo;
        this.journalEntryRepo = journalEntryRepo;
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(ObjectId id) {
        return userRepo.findById(id);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    public void saveUser(User user) {
        List<ObjectId> journalIds = user.getJournalEntries()
                .stream()
                .map(JournalEntry::getId)
                .collect(Collectors.toList());

        // Fetch journal entries from DB
        List<JournalEntry> journalEntries = journalEntryRepo.findAllById(journalIds);

        // Validate
        if (journalEntries.size() != journalIds.size()) {
            throw new IllegalArgumentException("Some journal entries not found for given IDs.");
        }

        // Replace placeholders with real entries
        user.setJournalEntries(journalEntries);

        // Save user
        userRepo.save(user);
    }

    public boolean updateUser(String userName, User user) {
        Optional<User> oldUser = userRepo.findByUserName(userName);
        if(oldUser.isPresent()) {
            oldUser.get().setUserName(!user.getUserName().isEmpty() ? user.getUserName() : userName);
            oldUser.get().setPassword(!user.getPassword().isEmpty() ? user.getPassword() : oldUser.get().getPassword());
            userRepo.save(oldUser.get());
            return true;
        }
        return false;
    }

    public boolean deleteUser(ObjectId id) {
        Optional<User> optionalJournalEntry = userRepo.findById(id);
        if(optionalJournalEntry.isPresent()) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

}
