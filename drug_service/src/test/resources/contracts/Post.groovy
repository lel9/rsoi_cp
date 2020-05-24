import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "post"
    request{
        method POST()
        url("/api/1.0/protected/drug") {
            body("{\n" +
                    "  \"activeSubstance\": \"string\",\n" +
                    "  \"atx\": \"string\",\n" +
                    "  \"certificateOwner\": \"string\",\n" +
                    "  \"composition\": \"string\",\n" +
                    "  \"contraindications\": \"string\",\n" +
                    "  \"description\": \"string\",\n" +
                    "  \"directionForUse\": \"string\",\n" +
                    "  \"form\": \"string\",\n" +
                    "  \"group\": \"string\",\n" +
                    "  \"indications\": \"string\",\n" +
                    "  \"interaction\": \"string\",\n" +
                    "  \"manufacturer\": \"string\",\n" +
                    "  \"overdose\": \"string\",\n" +
                    "  \"pharmacodynamics\": \"string\",\n" +
                    "  \"pharmacokinetics\": \"string\",\n" +
                    "  \"pregnancyAndLactation\": \"string\",\n" +
                    "  \"releaseFormVSDosage\": \"string\",\n" +
                    "  \"sideEffects\": \"string\",\n" +
                    "  \"specialInstruction\": \"string\",\n" +
                    "  \"storageLife\": \"string\",\n" +
                    "  \"storageСonditions\": \"string\",\n" +
                    "  \"tradeName\": \"string\",\n" +
                    "  \"transportationСonditions\": \"string\",\n" +
                    "  \"vacationFromPharmacies\": \"string\",\n" +
                    "  \"vehicleImpact\": \"string\",\n" +
                    "  \"withCaution\": \"string\"\n" +
                    "}")
        }
        headers {
            header("Content-Type", "application/json")
        }
    }
    response {
        status 201
    }
}