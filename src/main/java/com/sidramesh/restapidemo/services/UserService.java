package com.sidramesh.restapidemo.services;

import com.sidramesh.restapidemo.Entities.User;
import com.sidramesh.restapidemo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public User getById(long id){
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public User upsertFromGoogle(String googleSub, String email, String name, String picture) {
        return repo.findByGoogleSub(googleSub).map(u -> {
            boolean changed = false;
            if (email != null && !email.equals(u.getEmail())) { u.setEmail(email); changed = true; }
            if (name != null && !name.equals(u.getName())) { u.setName(name); changed = true; }
            if (picture != null && !picture.equals(u.getPicture())) { u.setPicture(picture); changed = true; }
            u.setLastLoginAt(Instant.now());
            return changed ? repo.save(u) : u;
        }).orElseGet(() -> {
            User u = new User();
            u.setGoogleSub(googleSub);
            u.setEmail(email);
            u.setName(name);
            u.setPicture(picture);
            u.setCreatedAt(Instant.now());
            u.setLastLoginAt(Instant.now());
            return repo.save(u);
        });
    }
}
