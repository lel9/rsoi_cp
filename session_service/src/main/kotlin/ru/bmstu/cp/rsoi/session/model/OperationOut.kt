package ru.bmstu.cp.rsoi.session.model

data class OperationOut(val entityId: String, val operationName: String) {
    val entityName: String = "user"
    val parentEntityName: String? = null
    val parentEntityId: String? = null
    val date = System.currentTimeMillis()
}