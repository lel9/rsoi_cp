import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "get analogs"
    request{
        method GET()
        url("/api/1.0/private/drug/search") {
        }
    }
    response {
        body("{\n" +
                "  \"results\": []" +
                "}")
        status 200
    }
}