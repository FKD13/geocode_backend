package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.User

class UserLoginWrapper(var email: String,
                       var password: String) : Wrapper() {
    constructor(user: User) : this(
            email = user.email,
            password = user.password
    )
}