package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import java.util.*

class UserRegisterWrapper(
        var email: String,
        var username: String,
        var password: String,
        var passwordRepeat: String?,
        var captcha: Optional<String>
) : Wrapper()