package ru.bmstu.cp.rsoi.session.model

data class OperationOut(private val entityId: String, private val operationName: String) {
    private val entityName: String = "user"
    private val parentEntityName: String? = null
    private val parentEntityId: String? = null
    private val date = System.currentTimeMillis()
}