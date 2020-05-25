

import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "put"
    request{
        method PUT()
        url("/api/1.0/protected/patient/5ec844100a975a009f2b9195") {
            body("{\n" +
                    "  \"birthday\": \"2020-05-25T16:43:14.144Z\",\n" +
                    "  \"cardId\": \"2222\",\n" +
                    "  \"sex\": \"f\"\n" +
                    "}")
        }
        headers {
            header("Content-Type", "application/json")
        }
    }
    response {
        status 200
    }
}