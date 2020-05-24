import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "get by id"
    request{
        method GET()
        url("/api/1.0/protected/profile/olga") {
        }
    }
    response {
        body("{\n" +
                "  \"displayName\": \"string\",\n" +
                "  \"id\": \"olga\",\n" +
                "  \"organization\": \"string\",\n" +
                "  \"profession\": \"string\"\n" +
                "}")
        headers {
            header("Content-Type", "application/json;charset=UTF-8")
        }
        status 200
    }
}