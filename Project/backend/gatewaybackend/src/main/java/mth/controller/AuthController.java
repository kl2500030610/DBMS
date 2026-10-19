package mth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(

            @RequestHeader("Authorization")
            String token
    ) {

        if(token.equals("Bearer mytoken123")) {

            return ResponseEntity.ok(
                    "Token Valid"
            );
        }

        return ResponseEntity
                .status(401)
                .body("Invalid Token");
    }
}