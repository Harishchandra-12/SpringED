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

    @GetMapping("/{userName}")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(@PathVariable String userName) {
        List<JournalEntry> list = journalEntryService.getAllEntries(userName);
        if(list!=null) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry, @PathVariable String userName) {
        try {
            if (journalEntryService.saveEntry(journalEntry, userName))
                return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/id/{myID}")
    public ResponseEntity<JournalEntry> getJournalEntryByID(@PathVariable ObjectId myID) {
        JournalEntry journalEntry = journalEntryService.getEntryById(myID).orElse(null);
        if (journalEntry != null)
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{userName}/{myID}")
    public ResponseEntity<String> deleteJournalEntryByID(@PathVariable ObjectId myID, @PathVariable String userName) {
        String result = journalEntryService.deleteEntry(myID , userName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/id/{userName}/{myId}")
    public ResponseEntity<String> updateJournalEntryByID(@PathVariable ObjectId myId, @RequestBody JournalEntry journalEntry, @PathVariable String userName) {
        String result = journalEntryService.updateEntry(myId, journalEntry , userName);
            return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
