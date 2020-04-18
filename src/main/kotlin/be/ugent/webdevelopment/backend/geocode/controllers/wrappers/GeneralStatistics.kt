package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

class GeneralStatistics(
        val locationsCount: Int,
        val userCount: Int,
        val visitsCount: Int,
        val countriesCount: Int
) : Wrapper()