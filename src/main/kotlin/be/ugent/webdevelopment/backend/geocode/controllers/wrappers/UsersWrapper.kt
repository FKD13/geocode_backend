package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.User

class UsersWrapper(var id: Int?,
                   var username: String?,
                   var avatarUrl: String?): Wrapper {
    constructor(user: User) : this(
            id = user.id,
            username = user.username,
            avatarUrl = user.avatarUrl
    )
}