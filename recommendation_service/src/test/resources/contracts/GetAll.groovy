import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "get all"
    request{
        method GET()
        url("/api/1.0/public/recommendation") {
            queryParameters {
                parameter("drugId", "0")
                parameter("page", "0")
                parameter("size", "1")
            }
        }
    }
    response {
        body("{\n" +
                "  \"page\": 0,\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"author\": {\n" +
                "        \"displayName\": \"string\",\n" +
                "        \"id\": \"olga\"\n" +
                "      },\n" +
                "      \"date\": 0,\n" +
                "      \"drugId\": \"0\",\n" +
                "      \"id\": \"0\",\n" +
                "      \"text\": \"text\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"size\": 1,\n" +
                "  \"totalElements\": 1,\n" +
                "  \"totalPages\": 1\n" +
                "}")
        status 200
    }
}