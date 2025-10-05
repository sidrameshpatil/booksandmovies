package com.sidramesh.restapidemo.Controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import com.sidramesh.restapidemo.Entities.User;
import com.sidramesh.restapidemo.auth.GoogleTokenVerifier;
import com.sidramesh.restapidemo.auth.JwtUtil;
import com.sidramesh.restapidemo.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final GoogleTokenVerifier googleVerifier;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final String cookieName;
    private final boolean cookieSecure;
    private final String cookieSameSite;
    private final int cookieMaxAgeSeconds;

    public AuthController(GoogleTokenVerifier googleVerifier,
                          UserService userService,
                          JwtUtil jwtUtil,
                          @Value("${app.cookie.name}") String cookieName,
                          @Value("${app.cookie.secure:false}") boolean cookieSecure,
                          @Value("${app.cookie.sameSite:Strict}") String cookieSameSite,
                          @Value("${app.jwt.expiration-minutes:60}") int cookieMaxAgeMinutes) {
        this.googleVerifier = googleVerifier;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.cookieName = cookieName;
        this.cookieSecure = cookieSecure;
        this.cookieSameSite = cookieSameSite;
        this.cookieMaxAgeSeconds = cookieMaxAgeMinutes * 60;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body, HttpServletResponse response) {
        String idToken = body.get("idToken");
        if (idToken == null || idToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error","Missing idToken"));
        }
        try {
            GoogleIdToken.Payload payload = googleVerifier.verify(idToken);
            if (payload == null) return ResponseEntity.status(401).body(Map.of("error","Invalid id token"));
            String googleSub = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");

            User user = userService.upsertFromGoogle(googleSub, email, name, picture);
            // create app JWT (subject = user id)
            String token = jwtUtil.generateToken(String.valueOf(user.getId()));

            // create cookie
            ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                    .httpOnly(true)
                    .secure(cookieSecure)
                    .sameSite(cookieSameSite)
                    .path("/")
                    .maxAge(Duration.ofSeconds(cookieMaxAgeSeconds))
                    .build();

            response.setHeader("Set-Cookie", cookie.toString());

            return ResponseEntity.ok(Map.of("user", Map.of(
                    "id", user.getId(),
                    "googleSub", user.getGoogleSub(),
                    "email", user.getEmail(),
                    "name", user.getName(),
                    "picture", user.getPicture()
            )));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error","Token verification or server error", "detail", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@CookieValue(name = "${app.cookie.name}", required = false) String token) {
        try {
            if (token == null) return ResponseEntity.status(401).body(Map.of("error","Not authenticated"));
            var claims = jwtUtil.validate(token);
            String userId = claims.getSubject();
               // find user by id (simple direct call)
            // you may want to call UserService to fetch user by id
            // (Add UserRepository.findById)
            // Example:
            var u = userService.getById(Long.valueOf(userId));
            if (u == null) return ResponseEntity.status(401).body(Map.of("error","User not found"));
            return ResponseEntity.ok(Map.of("user", Map.of(
                    "id", u.getId(),
                    "googleSub", u.getGoogleSub(),
                    "email", u.getEmail(),
                    "name", u.getName(),
                    "picture", u.getPicture()
            )));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error","Invalid token", "detail", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.noContent().build();
    }
}
