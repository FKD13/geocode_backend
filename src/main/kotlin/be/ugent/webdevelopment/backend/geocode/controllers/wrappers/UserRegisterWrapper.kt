package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.Email

class UserRegisterWrapper(var email: String,
                          var username: String,
                          var password: String,
                          var passwordRepeat: String?,
                          var captcha: Optional<String>) : Wrapper {
    constructor(user: User) : this(
            email = user.email,
            username = user.username,
            password = user.password,
            passwordRepeat = null,
            captcha = Optional.ofNullable(null)
    )
}