import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "patch"
    request{
        method PATCH()
        url("/api/1.0/protected/drug/0") {
            body("{\n" +
                    "  \"activeSubstance\": \"test\",\n" +
                    "  \"atx\": \"test\",\n" +
                    "  \"certificateOwner\": \"test\",\n" +
                    "  \"composition\": \"test\",\n" +
                    "  \"contraindications\": \"test\",\n" +
                    "  \"description\": \"test\",\n" +
                    "  \"directionForUse\": \"test\",\n" +
                    "  \"form\": \"test\",\n" +
                    "  \"group\": \"test\",\n" +
                    "  \"indications\": \"test\",\n" +
                    "  \"interaction\": \"test\",\n" +
                    "  \"manufacturer\": \"test\",\n" +
                    "  \"overdose\": \"test\",\n" +
                    "  \"pharmacodynamics\": \"test\",\n" +
                    "  \"pharmacokinetics\": \"test\",\n" +
                    "  \"pregnancyAndLactation\": \"test\",\n" +
                    "  \"releaseFormVSDosage\": \"test\",\n" +
                    "  \"sideEffects\": \"test\",\n" +
                    "  \"specialInstruction\": \"test\",\n" +
                    "  \"storageLife\": \"test\",\n" +
                    "  \"storageСonditions\": \"test\",\n" +
                    "  \"tradeName\": \"test\",\n" +
                    "  \"transportationСonditions\": \"test\",\n" +
                    "  \"vacationFromPharmacies\": \"test\",\n" +
                    "  \"vehicleImpact\": \"test\",\n" +
                    "  \"withCaution\": \"test\"\n" +
                    "}")
        }
        headers {
            header("Content-Type", "application/json")
        }
    }
    response {
        status 200
    }
}