package com.example.SpringED.controller;

import com.example.SpringED.entity.User;
import com.example.SpringED.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> list = userService.getEntries();
        if (list != null)
            return new ResponseEntity<>(list, HttpStatus.FOUND);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<User> getUserById(ObjectId myId) {
        Optional<User> optionalUser = userService.getUserById(myId);
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.FOUND))
                            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        userService.saveEntry(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("{userName}")
    public ResponseEntity<User> updateUser(@PathVariable String userName, @RequestBody User user) {

        if (userService.updateEntry(userName, user))
            return new ResponseEntity<>(user, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteUser(@PathVariable ObjectId myId) {
        if (userService.deleteEntry(myId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
