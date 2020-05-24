

import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "get all"
    request{
        method GET()
        url("/api/1.0/protected/patient") {
            queryParameters {
                parameter("cardId", "0000")
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
                "      \"id\": \"5ec844100a975a009f2b9195\",\n" +
                "      \"cardId\": \"0000\",\n" +
                "      \"birthday\": 0,\n" +
                "      \"sex\": \"m\"\n" +
                "    }\n" +
                "  ]\n" +
                "}")
        status 200
    }
}