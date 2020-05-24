import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "post"
    request{
        method POST()
        url("/api/1.0/protected/recommendation") {
            body("{\n" +
                    "  \"drugId\": \"string\",\n" +
                    "  \"text\": \"string\"\n" +
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