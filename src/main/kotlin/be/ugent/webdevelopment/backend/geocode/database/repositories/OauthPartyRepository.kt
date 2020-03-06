package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.OauthParty
import org.springframework.data.jpa.repository.JpaRepository

interface OauthPartyRepository : JpaRepository<OauthParty, Int> {
    fun findByOauthPartyName(string: String): OauthParty?
}