package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import java.util.*


class ResetWrapper(
        var oldPassword: Optional<String>,
        var password: Optional<String>,
        var passwordRepeat: Optional<String>
) : Wrapper()
