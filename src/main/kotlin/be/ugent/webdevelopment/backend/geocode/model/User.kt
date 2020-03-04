package be.ugent.webdevelopment.backend.geocode.model

import java.sql.Timestamp
import java.util.*


class User(var id: Long?, var oauth_id: String?, var oauth_secret: String?, var oauth_party: String?,
           var email: String?, var username: String?, var avatar_url: String?, var admin: Boolean?,
           var time: Date?) {
    constructor(id: Long, user: User) : this(id, user.oauth_id, user.oauth_secret, user.oauth_party, user.email,
            user.username, user.avatar_url, user.admin, user.time)

}