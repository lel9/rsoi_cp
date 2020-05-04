package ru.bmstu.cp.rsoi.session.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.bmstu.cp.rsoi.session.domain.entity.Client

@Repository
@Transactional
interface ClientRepository : CrudRepository<Client, String> {
}