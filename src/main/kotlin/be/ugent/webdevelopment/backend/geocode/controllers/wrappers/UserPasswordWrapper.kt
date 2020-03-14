package be.ugent.webdevelopment.backend.geocode.controllers.wrappers;

import be.ugent.webdevelopment.backend.geocode.database.models.User;
import be.ugent.webdevelopment.backend.geocode.database.models.UserPassword;

class UserPasswordWrapper(
        var user: User,
        var password: String
) : Wrapper {

    constructor(userPassword: UserPassword) : this(user = userPassword.user, password = userPassword.password)

}