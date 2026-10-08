package com.smartroute.service;

import com.smartroute.model.AuthRequest;
import com.smartroute.model.AuthResponse;
import com.smartroute.model.User;
import com.smartroute.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(AuthRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty() ||
            request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return new AuthResponse(false, "Email and password are required");
        }

        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            return new AuthResponse(false, "An account with this email already exists");
        }

        String name = request.getName() != null && !request.getName().trim().isEmpty()
                ? request.getName().trim() : email.split("@")[0];

        String hashedPassword = hashPassword(request.getPassword());
        User user = new User(name, email, hashedPassword);
        User saved = userRepository.save(user);

        String token = generateToken(saved);
        return new AuthResponse(true, "Registration successful", saved.getId(), saved.getName(), saved.getEmail(), token);
    }

    public AuthResponse login(AuthRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return new AuthResponse(false, "Email and password are required");
        }

        String email = request.getEmail().trim().toLowerCase();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return new AuthResponse(false, "Invalid email or password");
        }

        User user = userOpt.get();
        String hashedInput = hashPassword(request.getPassword());
        if (!user.getPassword().equals(hashedInput)) {
            return new AuthResponse(false, "Invalid email or password");
        }

        String token = generateToken(user);
        return new AuthResponse(true, "Login successful", user.getId(), user.getName(), user.getEmail(), token);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(password.hashCode());
        }
    }

    private String generateToken(User user) {
        String payload = user.getId() + ":" + user.getEmail() + ":" + System.currentTimeMillis() + ":" + UUID.randomUUID();
        return Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }
}
