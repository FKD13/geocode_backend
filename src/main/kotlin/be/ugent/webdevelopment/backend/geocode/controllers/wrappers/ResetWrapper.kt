package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import java.util.*


class ResetWrapper(
        var oldPassword: Optional<String>, //todo mag weg als we het enkel met cookie doen
        var password: Optional<String>,
        var passwordRepeat: Optional<String>
) : Wrapper()
