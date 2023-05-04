package com.example.fighteam.user.service;

import com.example.fighteam.user.domain.dto.FindPwdRequestDto;
import com.example.fighteam.user.domain.dto.JoinRequestDto;
import com.example.fighteam.user.domain.dto.LoginRequestDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public void signUp(JoinRequestDto joinRequestDto) {
        System.out.println("은정님");
    }

    public void login(LoginRequestDto loginRequestDto) {
        System.out.println("지안님");
    }

    public void findPassword(FindPwdRequestDto findPwdRequestDto) {

    }

}
