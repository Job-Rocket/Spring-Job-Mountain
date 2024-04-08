package com.example.job_mountain.incubent.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Incubent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String incuname;

    @Column(unique = true)
    private String incuId;

    private String pw;

    @Column(unique = true)
    private String email;

    @Column()
    private Integer age;

    @Column()
    private String companyin;

}
