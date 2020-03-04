package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.OauthParty
import org.springframework.data.repository.CrudRepository

interface OauthPartyRepository : CrudRepository<OauthParty, Long>