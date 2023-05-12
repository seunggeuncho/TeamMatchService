package com.example.fighteam.user.domain.repository;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            //userId 통일

    private String name;
    private String passwd;
    private String email;       // 로그인쓸 때 아이디
    private String tel;
    private String role;        //권한을 어디서 줄지 안정했지만 일단은 관리자와 구분하기위해 추가했습니다.

    @Builder
    public User(Long id, String email, String passwd, String name, String tel) {
        this.id = id;
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.tel = tel;
        this.role = role;



    }

}



