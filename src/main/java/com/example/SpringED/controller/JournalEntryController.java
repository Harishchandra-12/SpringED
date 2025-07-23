package com.example.SpringED.controller;

import com.example.SpringED.entity.JournalEntry;
import com.example.SpringED.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    JournalEntryService journalEntryService;

    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAll() {
        List<JournalEntry> list = journalEntryService.getEntries();
        if (list != null)
            return new ResponseEntity<>(list, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry) {
        try {
            journalEntryService.saveEntry(journalEntry);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myID}")
    public ResponseEntity<JournalEntry> getJournalEntryByID(@PathVariable ObjectId myID) {
        JournalEntry journalEntry = journalEntryService.getEntryById(myID).orElse(null);
        if (journalEntry != null)
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myID}")
    public ResponseEntity<Boolean> deleteJournalEntryByID(@PathVariable ObjectId myID) {
        if (journalEntryService.deleteEntry(myID))
            return new ResponseEntity<>(true, HttpStatus.OK);
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{myID}")
    public ResponseEntity<JournalEntry> updateJournalEntryByID(@PathVariable ObjectId myID, @RequestBody JournalEntry journalEntry) {
        if (journalEntryService.updateEntry(myID, journalEntry))
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        return new ResponseEntity<>(journalEntry, HttpStatus.NOT_FOUND);
    }

}
