package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

class UserStatistics(
        val visitedLocationsCount: Int,
        val visitedCountriesCount: Int,
        val visitedCount: Int
) : Wrapper()