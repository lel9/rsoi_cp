package ru.bmstu.cp.rsoi.session.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.bmstu.cp.rsoi.session.domain.entity.Role


@Repository
interface RoleRepository : CrudRepository<Role, String> {
    fun findByRolename(name: String): Role?
}