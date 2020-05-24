import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "get analogs"
    request{
        method GET()
        url("/api/1.0/private/drug/0/analogs") {
        }
    }
    response {
        body("{\n" +
                "  \"results\": [\n" +
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