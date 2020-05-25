

import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "post"
    request{
        method POST()
        url("/api/1.0/protected/patient") {
            body("{\n" +
                    "  \"birthday\": \"2020-05-25T16:43:14.144Z\",\n" +
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