import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "get all"
    request{
        method GET()
        url("/api/1.0/public/drug") {
            queryParameters {
                parameter("text", "string")
                parameter("page", "0")
                parameter("size", "1")
            }
        }
    }
    response {
        body("{\n" +
                "  \"totalPages\": 1,\n" +
                "  \"totalElements\": 1,\n" +
                "  \"page\": 0,\n" +
                "  \"size\": 1,\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"id\": \"0\",\n" +
                "      \"tradeName\": \"string\",\n" +
                "      \"releaseFormVSDosage\": \"string\",\n" +
                "      \"manufacturer\": \"string\"\n" +
                "    }\n" +
                "  ]\n" +
                "}")
        status 200
    }
}