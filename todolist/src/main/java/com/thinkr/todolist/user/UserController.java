package com.thinkr.todolist.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserModel user) {
        var username = userRepository.findByUsername(user.getUsername());
        if(username != null) {
            return status(HttpStatus.CONFLICT).body("User already created");
        }
        var userCreated = userRepository.save(user);
        return status(HttpStatus.CREATED).body(userCreated);
    }

    @GetMapping
    public ResponseEntity<String> get() {
        return status(HttpStatus.OK).body("Service is working");
    }
}
