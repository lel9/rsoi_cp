package ru.bmstu.cp.rsoi.drug.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "drugs")
@Data
public class Drug {

    @Id
    private String id;

    @Field(value = "trade_name")
    private String tradeName;

    @Field(value = "active_substance")
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

    @Field(value = "with_caution")
    private String withCaution;

    @Field(value = "pregnancy_and _lactation")
    private String pregnancyAndLactation;

    @Field(value = "direction_for_use")
    private String directionForUse;

    @Field(value = "side_effects")
    private String sideEffects;

    private String overdose;

    private String interaction;

    @Field(value = "special_instruction")
    private String specialInstruction;

    @Field(value = "vehicles_impact")
    private String vehicleImpact;

    @Field(value = "release_form_vs_dosage")
    private String releaseFormVSDosage;

    @Field(value = "transportation_conditions")
    private String transportationСonditions;

    @Field(value = "storage_conditions")
    private String storageСonditions;

    @Field(value = "storage_life")
    private String storageLife;

    @Field(value = "vacation_from_pharmacies")
    private String vacationFromPharmacies;

    private String manufacturer;

    @Field(value = "certificate_owner")
    private String certificateOwner;

}
