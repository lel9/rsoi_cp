

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "get by ids"
    request{
        method GET()
        url("/api/1.0/protected/patient/byIds") {
            queryParameters {
                parameter("ids", "5ec844100a975a009f2b9195")
            }
        }
    }
    response {
        body("{\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"birthday\": \"2020-05-25T16:43:14.144Z\",\n" +
                "      \"cardId\": \"0000\",\n" +
                "      \"id\": \"5ec844100a975a009f2b9195\",\n" +
                "      \"sex\": \"m\"\n" +
                "    }\n" +
                "  ]\n" +
                "}")
        status 200
    }
}
