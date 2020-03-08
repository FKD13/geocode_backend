package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.User
import java.time.LocalDateTime

class UserWrapper(
        id : Int?,
        username: String?,
        avatarUrl: String?,
        var oauthParty: String?,
        var email: String?,
        var admin: Boolean?,
        var time: LocalDateTime?) : UsersWrapper(id, username, avatarUrl){

    constructor(user: User): this(
            id = user.id,
            username = user.username,
            avatarUrl = user.avatarUrl,
            oauthParty = user.oauthParty.oauthPartyName,
            email = user.email,
            admin = user.admin,
            time = user.time
    )
}

