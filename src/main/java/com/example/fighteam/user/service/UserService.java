package com.example.fighteam.user.service;
import com.example.fighteam.user.domain.repository.User;
import com.example.fighteam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signUp(User user){
        user.setRole("USER");
        userRepository.save(user);
    }

    public User loginUser(String email, String passwd){
        User user = userRepository.selectUserInfo(email,passwd);
        return user;
    }


}

