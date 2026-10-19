package mth.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mth.models.Menus;
import mth.models.Rolesmapping;
import mth.models.Users;
import mth.repository.MenusRepository;
import mth.repository.RolesmappingRepository;
import mth.repository.UsersRepository;

@Service
public class UsersService {

    @Autowired
    UsersRepository UR;

    @Autowired
    MenusRepository MR;

    @Autowired
    RolesmappingRepository RMR;

    @Autowired
    JwtService JWT;

    // ─── AUTH ────────────────────────────────────────────────────────────────────

    public Object signup(Users U) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Users> existing = UR.findByEmail(U.getEmail());
            if (existing.isPresent()) {
                response.put("code", 501);
                response.put("message", "Email ID already registered");
            } else {
                U.setRole(1);
                U.setStatus(1);
                UR.save(U);
                response.put("code", 200);
                response.put("message", "User account has been created.");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return response;
    }

    public Object signin(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            String username = data.get("username").toString();
            String password = data.get("password").toString();

            Optional<Users> userOpt = UR.findByEmailAndPassword(username, password);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                response.put("code", 200);
                response.put("jwt", JWT.generateJWT(username, user.getRole()));
            } else {
                response.put("code", 404);
                response.put("message", "Invalid Credentials!");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return response;
    }

    public Object uinfo(String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> payload = JWT.validateJWT(token);
            String email = (String) payload.get("username");

            Optional<Users> userOpt = UR.findByEmail(email);
            if (!userOpt.isPresent()) {
                response.put("code", 404);
                response.put("message", "User not found");
                return response;
            }
            Users U = userOpt.get();

            List<Rolesmapping> mappings = RMR.findByRole((long) U.getRole());
            List<Long> mids = mappings.stream().map(Rolesmapping::getMid).collect(Collectors.toList());
            List<Menus> menuList = MR.findAll().stream()
                    .filter(m -> mids.contains(m.getMid()))
                    .collect(Collectors.toList());

            response.put("code", 200);
            response.put("fullname", U.getFullname());
            response.put("role", U.getRole());
            response.put("menulist", menuList);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // ─── CRUD ────────────────────────────────────────────────────────────────────

    // READ - all users
    public Object getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Users> users = UR.findAll();
            response.put("code", 200);
            response.put("data", users);
            response.put("count", users.size());
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // READ - single user by ID
    public Object getUserById(String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Users> userOpt = UR.findById(id);
            if (userOpt.isPresent()) {
                response.put("code", 200);
                response.put("data", userOpt.get());
            } else {
                response.put("code", 404);
                response.put("message", "User not found with id: " + id);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // CREATE - add a new user (admin use; skips JWT, allows setting role)
    public Object createUser(Users U) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (U.getEmail() == null || U.getEmail().trim().isEmpty()) {
                response.put("code", 400);
                response.put("message", "Email is required");
                return response;
            }
            Optional<Users> existing = UR.findByEmail(U.getEmail());
            if (existing.isPresent()) {
                response.put("code", 501);
                response.put("message", "Email ID already registered");
                return response;
            }
            if (U.getStatus() == 0) U.setStatus(1); // default active
            UR.save(U);
            response.put("code", 200);
            response.put("message", "User created successfully");
            response.put("data", U);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // UPDATE - update user details by ID
    public Object updateUser(String id, Users updated) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Users> userOpt = UR.findById(id);
            if (!userOpt.isPresent()) {
                response.put("code", 404);
                response.put("message", "User not found with id: " + id);
                return response;
            }

            Users existing = userOpt.get();

            // If email is being changed, check uniqueness
            if (updated.getEmail() != null
                    && !updated.getEmail().equalsIgnoreCase(existing.getEmail())) {
                Optional<Users> emailTaken = UR.findByEmail(updated.getEmail());
                if (emailTaken.isPresent()) {
                    response.put("code", 501);
                    response.put("message", "Email ID already in use");
                    return response;
                }
                existing.setEmail(updated.getEmail());
            }

            if (updated.getFullname() != null) existing.setFullname(updated.getFullname());
            if (updated.getPhone()    != null) existing.setPhone(updated.getPhone());
            if (updated.getPassword() != null) existing.setPassword(updated.getPassword());
            if (updated.getRole()     != 0)    existing.setRole(updated.getRole());
            if (updated.getStatus()   != 0)    existing.setStatus(updated.getStatus());

            UR.save(existing);
            response.put("code", 200);
            response.put("message", "User updated successfully");
            response.put("data", existing);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // DELETE - remove user by ID
    public Object deleteUser(String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Users> userOpt = UR.findById(id);
            if (!userOpt.isPresent()) {
                response.put("code", 404);
                response.put("message", "User not found with id: " + id);
                return response;
            }
            UR.deleteById(id);
            response.put("code", 200);
            response.put("message", "User deleted successfully");
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // STATUS TOGGLE - activate / deactivate user
    public Object updateStatus(String id, int status) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Users> userOpt = UR.findById(id);
            if (!userOpt.isPresent()) {
                response.put("code", 404);
                response.put("message", "User not found with id: " + id);
                return response;
            }
            Users U = userOpt.get();
            U.setStatus(status);
            UR.save(U);
            response.put("code", 200);
            response.put("message", status == 1 ? "User activated" : "User deactivated");
            response.put("data", U);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return response;
    }
}
