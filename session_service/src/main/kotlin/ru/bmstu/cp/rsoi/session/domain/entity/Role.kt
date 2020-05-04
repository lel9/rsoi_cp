package ru.bmstu.cp.rsoi.session.domain.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import org.springframework.security.core.GrantedAuthority
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role constructor(private val rolename: String) : GrantedAuthority {

    @Id
    @GeneratedValue
    var id: UUID = UUID.randomUUID()

    @JsonBackReference
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    val users: List<User>? = null

    override fun getAuthority(): String {
        return this.rolename
    }

}