package ru.bmstu.cp.rsoi.drug.model;

import lombok.Data;

@Data
public class DrugIn {

    private String tradeName;

    private String activeSubstance;

    private String form;

    private String composition;

    private String description;

    private String group;

    private String atx;

    private String pharmacodynamics;

    private String pharmacokinetics;

    private String indications;

    private String contraindications;

    private String withCaution;

    private String pregnancyAndLactation;

    private String directionForUse;

    private String sideEffects;

    private String overdose;

    private String interaction;

    private String specialInstruction;

    private String vehicleImpact;

    private String releaseFormVSDosage;

    private String transportationСonditions;

    private String storageСonditions;

    private String storageLife;

    private String vacationFromPharmacies;

    private String manufacturer;

    private String certificateOwner;

}
