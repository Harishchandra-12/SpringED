package com.example.SpringED.controller;

import com.example.SpringED.entity.User;
import com.example.SpringED.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    UserService userService;

    AdminController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        if (users.isEmpty() || users == null ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PostMapping("/create-admin-user")
    public ResponseEntity<User> createAdmin(@RequestBody User user){
        User savedAdmin = userService.saveAdmin(user);
        if(savedAdmin!=null)
            return new ResponseEntity<>(savedAdmin,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
