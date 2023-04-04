package Kopeav.MusicApp.Controllers;

import Kopeav.MusicApp.DTO.ErrorDTO;
import Kopeav.MusicApp.DTO.LoginDTO;
import Kopeav.MusicApp.DTO.RegisterDTO;
import Kopeav.MusicApp.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO request) {
        try {
            var response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            var errorResponse = ErrorDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        try {
            var response = service.login(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            var errorResponse = ErrorDTO.builder()
                    .message("Wrong username or password")
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            var errorResponse = ErrorDTO.builder()
                    .message(" ")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/checkToken")
    public ResponseEntity<?> checkToken(@RequestParam("token") String token){
        if (service.checkToken(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}