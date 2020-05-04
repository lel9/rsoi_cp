package ru.bmstu.cp.rsoi.session.domain.entity

import java.io.Serializable
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "users")
class User(val username: String, val password: String) : Serializable {

    @Id
    @GeneratedValue
    var id: UUID = UUID.randomUUID()

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roles: MutableList<Role> = arrayListOf()

}

