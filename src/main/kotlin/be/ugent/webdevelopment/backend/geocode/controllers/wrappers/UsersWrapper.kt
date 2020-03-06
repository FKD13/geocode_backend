package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.User

class UsersWrapper(var username: String?,
                   var avatarUrl: String?) {
    constructor(user: User) : this(
            username = user.username,
            avatarUrl = user.avatarUrl
    )
}