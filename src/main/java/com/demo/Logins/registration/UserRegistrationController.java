package com.demo.Logins.registration;


import com.demo.Logins.appUser.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class UserRegistrationController {

    // could possibly need to be deleted
    @Autowired
    private final RegistrationService registrationService;

    @Autowired
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        try {
            String token = registrationService.register(request);
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping(path = "confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        try {
            String response = registrationService.validateToken(token);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "logIn")
    public ResponseEntity<String> logIn(@RequestParam("email") String email,
                                        @RequestParam("password") String password) {
       try {
            String response = userService.loginUser(email, password);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }

    }
}
