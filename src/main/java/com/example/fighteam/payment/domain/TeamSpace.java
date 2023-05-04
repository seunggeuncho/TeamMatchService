package com.example.fighteam.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TeamSpace {

    @Id @GeneratedValue
    @Column(name = "teamspace_id")
    private Long id;

}
