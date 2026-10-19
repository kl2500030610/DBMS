package mth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mth.models.Users;
import mth.services.UsersService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/authservice")
public class UsersController {

    @Autowired
    UsersService US;

    // ─── AUTH ────────────────────────────────────────────────────────────────────

    @PostMapping("/signup")
    public Object signup(@RequestBody Users U) {
        return US.signup(U);
    }

    @PostMapping("/signin")
    public Object signin(@RequestBody Map<String, Object> data) {
        return US.signin(data);
    }

    @GetMapping("/uinfo")
    public Object uinfo(@RequestHeader("Token") String token) {
        return US.uinfo(token);
    }

    @GetMapping("/test")
    public String testMethod() {
        return "Welcome I'm fine";
    }

    // ─── CRUD ────────────────────────────────────────────────────────────────────

    // READ - get all users
    // GET /authservice/allusers
    @GetMapping("/allusers")
    public Object getAllUsers() {
        return US.getAllUsers();
    }

    // READ - get user by ID
    // GET /authservice/users/{id}
    @GetMapping("/users/{id}")
    public Object getUserById(@PathVariable String id) {
        return US.getUserById(id);
    }

    // CREATE - add a new user (admin endpoint; allows setting role/status directly)
    // POST /authservice/users
    @PostMapping("/users")
    public Object createUser(@RequestBody Users U) {
        return US.createUser(U);
    }

    // UPDATE - update user by ID (full update)
    // PUT /authservice/users/{id}
    @PutMapping("/users/{id}")
    public Object updateUser(@PathVariable String id, @RequestBody Users U) {
        return US.updateUser(id, U);
    }

    // DELETE - delete user by ID
    // DELETE /authservice/users/{id}
    @DeleteMapping("/users/{id}")
    public Object deleteUser(@PathVariable String id) {
        return US.deleteUser(id);
    }

    // STATUS TOGGLE - activate (1) or deactivate (0) a user
    // PATCH /authservice/users/{id}/status?value=0
    @PatchMapping("/users/{id}/status")
    public Object updateStatus(@PathVariable String id, @RequestParam int value) {
        return US.updateStatus(id, value);
    }
}
