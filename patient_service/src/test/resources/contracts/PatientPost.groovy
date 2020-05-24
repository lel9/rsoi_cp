

import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "post"
    request{
        method POST()
        url("/api/1.0/protected/patient") {
            body("{\n" +
                    "  \"birthday\": 0,\n" +
                    "  \"cardId\": \"1111\",\n" +
                    "  \"sex\": \"m\"\n" +
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