package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime
import java.util.*

class UserWrapper(
        id : Optional<Int>,
        username: Optional<String>,
        avatarUrl: Optional<String>,
        var email: Optional<String>,
        var admin: Optional<Boolean>,
        var time: Optional<Date>) : UsersWrapper(id, username, avatarUrl){

    constructor(user: User): this(
            id = Optional.of(user.id),
            username = Optional.of(user.username),
            avatarUrl = Optional.of(user.avatarUrl),
            email = Optional.of(user.email),
            admin = Optional.of(user.admin),
            time = Optional.of(user.time)
    )
}

