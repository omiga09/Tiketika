package com.tiketika.Tiketika.common.helper;

import com.tiketika.Tiketika.common.onboarding.entities.KYCDetail;

public class KYCValidator {

    public static void validate(KYCDetail kyc) {
        if (kyc.getUserGroupType() == null) {
            throw new IllegalArgumentException("User group type is required");
        }

        switch (kyc.getUserGroupType()) {
            case INDIVIDUAL -> validateIndividual(kyc);
            case ORGANIZATION -> validateOrganization(kyc);
        }
    }

    private static void validateIndividual(KYCDetail kyc) {
        require(kyc.getContactPerson(), "contactPerson");
        require(kyc.getPrimaryEmail(), "primaryEmail");
        require(kyc.getPersonalPhone(), "personalPhone");
        require(kyc.getAddress(), "address");
        require(kyc.getCountry(), "country");
    }

    private static void validateOrganization(KYCDetail kyc) {
        require(kyc.getBusinessName(), "businessName");
        require(kyc.getTin(), "tin");
        require(kyc.getVrn(), "vrn");
        require(kyc.getContactPerson(), "contactPerson");
        require(kyc.getPrimaryEmail(), "primaryEmail");
        require(kyc.getOfficePhone(), "officePhone");
        require(kyc.getPersonalPhone(), "personalPhone");
        require(kyc.getAddress(), "address");
        require(kyc.getCountry(), "country");
        // website optional
    }

    private static void require(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required field: " + field);
        }
    }
}
