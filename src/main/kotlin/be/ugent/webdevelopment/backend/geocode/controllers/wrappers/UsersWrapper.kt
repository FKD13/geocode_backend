package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.User
import java.util.*

open class UsersWrapper(var id: Optional<Int>,
                   var username: Optional<String>,
                   var avatarUrl: Optional<String>): Wrapper {
    constructor(user: User) : this(
            id = Optional.of(user.id),
            username = Optional.of(user.username),
            avatarUrl = Optional.of(user.avatarUrl)
    )
}