package be.ugent.webdevelopment.backend.geocode.controllers.wrappers;

import be.ugent.webdevelopment.backend.geocode.database.models.User;
import be.ugent.webdevelopment.backend.geocode.database.models.UserPassword;

class UserPasswordWrapper(
        var user: User,
        var password: String
) : Wrapper {
    contructor(userPass: UserPassword) : this(
            user = userPass.user,
            password = userPass.password
        )
}