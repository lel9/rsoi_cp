import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "put reception"
    request{
        method PUT()
        url("/api/1.0/protected/profile/olga") {
            body("{\n" +
                    "  \"displayName\": \"string\",\n" +
                    "  \"organization\": \"string\",\n" +
                    "  \"profession\": \"string\"\n" +
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