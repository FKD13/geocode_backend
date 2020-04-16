package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

class UserLoginWrapper(
        var email: String,
        var password: String
) : Wrapper()