import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "get all reception"
    request{
        method GET()
        url("/api/1.0/public/patient/5ec844100a975a009f2b9195/reception") {
        }
    }
    response {
        body("{\n" +
                "    \"receptions\": [\n" +
                "        {\n" +
                "            \"date\": \"2020-05-25T16:43:14.144Z\",\n" +
                "            \"diagnosis\": {\n" +
                "            \"text\": \"string\"\n" +
                "        },\n" +
                "            \"drugs\": [\n" +
                "                {\n" +
                "                    \"id\": \"0\",\n" +
                "                    \"manufacturer\": \"string\",\n" +
                "                    \"releaseFormVSDosage\": \"string\",\n" +
                "                    \"tradeName\": \"string\"\n" +
                "                }\n" +
                "        ],\n" +
                "            \"id\": \"0\",\n" +
                "            \"state\": {\n" +
                "            \"diseaseAnamnesis\": \"string\",\n" +
                "            \"examinationsResults\": \"string\",\n" +
                "            \"lifeAnamnesis\": \"string\",\n" +
                "            \"months\": 0,\n" +
                "            \"objectiveInspection\": \"string\",\n" +
                "            \"plaints\": \"string\",\n" +
                "            \"sex\": \"m\",\n" +
                "            \"specialistsConclusions\": \"string\",\n" +
                "            \"years\": 0\n" +
                "        }\n" +
                "        }\n" +
                "]\n" +
                "}")
        status 200
    }
}