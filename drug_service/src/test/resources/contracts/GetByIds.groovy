import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "get by ids"
    request{
        method GET()
        url("/api/1.0/protected/drug/byIds") {
            queryParameters {
                parameter("ids", "0")
                parameter("ids", "1")
            }
        }
    }
    response {
        body("{\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"id\": \"0\",\n" +
                "      \"tradeName\": \"string\",\n" +
                "      \"releaseFormVSDosage\": \"string\",\n" +
                "      \"manufacturer\": \"string\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"1\",\n" +
                "      \"tradeName\": \"test\",\n" +
                "      \"releaseFormVSDosage\": \"string\",\n" +
                "      \"manufacturer\": \"string\"\n" +
                "    }\n" +
                "  ]\n" +
                "}")
        status 200
    }
}
