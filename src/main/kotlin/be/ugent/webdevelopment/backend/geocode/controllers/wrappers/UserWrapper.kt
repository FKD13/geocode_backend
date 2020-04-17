package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import java.util.*

class UserWrapper(
        var id: Optional<Int>,
        var username: Optional<String>,
        var avatarId: Optional<Int>,
        var email: Optional<String>,
        var admin: Optional<Boolean>,
        var time: Optional<Date>
) : Wrapper()