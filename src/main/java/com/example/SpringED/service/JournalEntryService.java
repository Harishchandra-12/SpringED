package com.example.SpringED.service;

import com.example.SpringED.entity.JournalEntry;
import com.example.SpringED.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    JournalEntryRepo journalEntryRepo;

    public JournalEntryService(JournalEntryRepo journalEntryRepo) {
        this.journalEntryRepo = journalEntryRepo;
    }

    public List<JournalEntry> getEntries() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> getEntryById(ObjectId id) {
        return journalEntryRepo.findById(id);
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntry.setDate(LocalDateTime.now());
        journalEntryRepo.save(journalEntry);
    }

    public boolean updateEntry(ObjectId id, JournalEntry journalEntry) {
        Optional<JournalEntry> oldEntry = getEntryById(id);
        if(oldEntry.isPresent()) {
            oldEntry.get().setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : oldEntry.get().getContent());
            oldEntry.get().setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : oldEntry.get().getTitle());
            journalEntryRepo.save(oldEntry.get());
            return true;
        }
        return false;
    }

    public boolean deleteEntry(ObjectId id) {
        Optional<JournalEntry> optionalJournalEntry = journalEntryRepo.findById(id);
        if(optionalJournalEntry.isPresent()) {
            journalEntryRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
