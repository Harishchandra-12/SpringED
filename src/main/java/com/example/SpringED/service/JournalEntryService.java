package com.example.SpringED.service;

import com.example.SpringED.entity.JournalEntry;
import com.example.SpringED.entity.User;
import com.example.SpringED.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    JournalEntryRepo journalEntryRepo;
    UserService userService;

    public JournalEntryService(JournalEntryRepo journalEntryRepo, UserService userService) {
        this.journalEntryRepo = journalEntryRepo;
        this.userService = userService;
    }

    public List<JournalEntry> getAllEntries(String userName) {
        Optional<User> user = userService.getUserByUserName(userName);
        return user.map(User::getJournalEntries).orElse(null);
    }

    public Optional<JournalEntry> getEntryById(ObjectId id) {
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean saveEntry(JournalEntry journalEntry, String userName) {
        Optional<User> optionalUser = userService.userRepo.findByUserName(userName);

        if(optionalUser.isPresent()) {
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            optionalUser.get().getJournalEntries().add(saved);
            userService.saveUser(optionalUser.get());
            return true;
        }
        return false;
    }

    public String updateEntry(ObjectId id, JournalEntry journalEntry, String userName) {
        Optional<User> optionalUser = userService.getUserByUserName(userName);
        Optional<JournalEntry> optionalEntry = getEntryById(id);
        if(optionalUser.isPresent()) {
            if(optionalEntry.isPresent()) {
                optionalUser.get().getJournalEntries().removeIf(x -> x.getId().equals(id));
                optionalEntry.get().setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : optionalEntry.get().getContent());
                optionalEntry.get().setTitle(!journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : optionalEntry.get().getTitle());
                JournalEntry saved = journalEntryRepo.save(optionalEntry.get());
                optionalUser.get().getJournalEntries().add(saved);
                return "Successfully updated the Journal entry for the User " + userName + " with the Id" + id;
            }
            return "Hello" + userName + " there is no journal entry with given Id :  " + id;

        }
        return "There is no user with User Name" + userName;
    }

    public String deleteEntry(ObjectId myId , String userName) {
        Optional<JournalEntry> optionalJournalEntry = journalEntryRepo.findById(myId);
        Optional<User> optionalUser = userService.getUserByUserName(userName);
        if(optionalUser.isPresent()) {
            if(optionalJournalEntry.isPresent()) {
                optionalUser.get().getJournalEntries().removeIf(x -> x.getId().equals(myId));
                journalEntryRepo.deleteById(myId);
                userService.saveUser(optionalUser.get());
                return "Successfully deleted the Journal entry for the User " + userName + " with the Id" + myId;
            }
            return "Hello" + userName + " there is no journal entry with given Id :  " + myId;
        }
        return "There is no user with User Name" + userName;
    }
}
