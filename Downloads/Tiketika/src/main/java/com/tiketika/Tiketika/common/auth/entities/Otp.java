package com.tiketika.Tiketika.common.auth.entities;


import com.tiketika.Tiketika.common.auditing.BaseAuditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="app_otp")
public class Otp extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @OneToOne
    private User user;

    private Long expiryTime;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean verified = false;
}