package com.tiketika.engine.common.onboarding.entities;

import com.tiketika.engine.common.auditing.BaseAuditable;
import com.tiketika.engine.common.onboarding.enums.UserGroupType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="app_kyc_detail")
public class KYCDetail extends BaseAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tin;
    private String vrn;
    private String businessName;
    private String website;

    private String contactPerson;

    @Email
    private String primaryEmail;

    @Email
    private String supportEmail;

    private String officePhone;

    private String personalPhone;

    private String address;

    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_group_type")
    private UserGroupType userGroupType;

    @OneToOne
    @JoinColumn(name = "entity_owner_id", nullable = false)
    private EntityOwner entityOwner;
}
