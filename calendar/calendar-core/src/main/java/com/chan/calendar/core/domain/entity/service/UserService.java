package com.chan.calendar.core.domain.entity.service;

import com.chan.calendar.core.domain.dto.UserCreateReq;
import com.chan.calendar.core.domain.entity.User;
import com.chan.calendar.core.domain.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User create(UserCreateReq userCreateReq) {
        userRepository.findByEmail(userCreateReq.getEmail())
                .ifPresent( u ->  {
                    throw new RuntimeException("user already exists");
                });

        return userRepository.save(userCreateReq.toUser());
    }

    @Transactional
    public Optional<User> findPwdMatchUser(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> user.getPassword().equals(password) ? user : null);
    }
}
