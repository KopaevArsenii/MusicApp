package Kopeav.MusicApp.Services;

import Kopeav.MusicApp.DTO.AuthenticationDTO;
import Kopeav.MusicApp.DTO.LoginDTO;
import Kopeav.MusicApp.DTO.RegisterDTO;
import Kopeav.MusicApp.Models.Role;
import Kopeav.MusicApp.Models.User;
import Kopeav.MusicApp.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationDTO register(RegisterDTO request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is taken");
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationDTO.builder().token(jwtToken).build();
    }
    public AuthenticationDTO login(LoginDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationDTO.builder().token(jwtToken).build();
    }

    public boolean checkToken(String token){
        return jwtService.isTokenValid(token);
    }
}