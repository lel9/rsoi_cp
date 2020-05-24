

import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "get by id"
    request{
        method GET()
        url("/api/1.0/protected/patient/5ec844100a975a009f2b9195") {
        }
    }
    response {
        body("{\n" +
                "  \"birthday\": 0,\n" +
                "  \"cardId\": \"0000\",\n" +
                "  \"id\": \"5ec844100a975a009f2b9195\",\n" +
                "  \"receptions\": [\n" +
                "    {\n" +
                "      \"date\": 0,\n" +
                "      \"diagnosis\": {\n" +
                "        \"text\": \"string\"\n" +
                "      },\n" +
                "      \"drugs\": [\n" +
                "        {\n" +
                "          \"id\": \"0\",\n" +
                "          \"manufacturer\": \"string\",\n" +
                "          \"releaseFormVSDosage\": \"string\",\n" +
                "          \"tradeName\": \"string\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"id\": \"0\",\n" +
                "      \"state\": {\n" +
                "        \"diseaseAnamnesis\": \"string\",\n" +
                "        \"examinationsResults\": \"string\",\n" +
                "        \"lifeAnamnesis\": \"string\",\n" +
                "        \"months\": 0,\n" +
                "        \"objectiveInspection\": \"string\",\n" +
                "        \"plaints\": \"string\",\n" +
                "        \"sex\": \"m\",\n" +
                "        \"specialistsConclusions\": \"string\",\n" +
                "        \"years\": 0\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"sex\": \"m\"\n" +
                "}")
        status 200
    }
}