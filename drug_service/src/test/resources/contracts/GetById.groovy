import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "get by id"
    request{
        method GET()
        url("/api/1.0/public/drug/0") {
        }
    }
    response {
        body("{\"activeSubstance\":\"string\"," +
                "\"atx\":\"string\"," +
                "\"certificateOwner\":\"string\"," +
                "\"composition\":\"string\"," +
                "\"contraindications\":\"string\"," +
                "\"description\":\"string\"," +
                "\"directionForUse\":\"string\"," +
                "\"form\":\"string\"," +
                "\"group\":\"string\"," +
                "\"id\":\"0\"," +
                "\"indications\":\"string\"," +
                "\"interaction\":\"string\"," +
                "\"manufacturer\":\"string\"," +
                "\"overdose\":\"string\"," +
                "\"pharmacodynamics\":\"string\"," +
                "\"pharmacokinetics\":\"string\"," +
                "\"pregnancyAndLactation\":\"string\"," +
                "\"releaseFormVSDosage\":\"string\"," +
                "\"sideEffects\":\"string\"," +
                "\"specialInstruction\":\"string\"," +
                "\"storageLife\":\"string\"," +
                "\"storageСonditions\":\"string\"," +
                "\"tradeName\":\"string\"," +
                "\"transportationСonditions\":\"string\"," +
                "\"vacationFromPharmacies\":\"string\"," +
                "\"vehicleImpact\":\"string\"," +
                "\"withCaution\":\"string\"" +
                "}")
        status 200
    }
}