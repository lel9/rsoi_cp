import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "post reception"
    request{
        method POST()
        url("/api/1.0/protected/patient/5ec815d30a975a009f2b9191/reception") {
            body("{\n" +
                    "  \"date\": 0,\n" +
                    "  \"diagnosis\": {\n" +
                    "    \"text\": \"string\"\n" +
                    "  },\n" +
                    "  \"drugs\": [\n" +
                    "    {\n" +
                    "      \"id\": \"string\",\n" +
                    "      \"manufacturer\": \"string\",\n" +
                    "      \"releaseFormVSDosage\": \"string\",\n" +
                    "      \"tradeName\": \"string\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"state\": {\n" +
                    "    \"diseaseAnamnesis\": \"string\",\n" +
                    "    \"examinationsResults\": \"string\",\n" +
                    "    \"lifeAnamnesis\": \"string\",\n" +
                    "    \"objectiveInspection\": \"string\",\n" +
                    "    \"plaints\": \"string\",\n" +
                    "    \"specialistsConclusions\": \"string\"\n" +
                    "  }\n" +
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