package com.example.security01.service;

import com.example.security01.dto.UserDto;
import com.example.security01.model.User;
import com.example.security01.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(UserDto dto) {
        User user = dto.toEntity(dto);
        userRepository.save(user);
    }

}
