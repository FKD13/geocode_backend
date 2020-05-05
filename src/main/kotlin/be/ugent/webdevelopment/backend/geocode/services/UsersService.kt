package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.DATATYPE
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.DeleteWrappper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.PrivacyWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.*
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UsersService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var tourRepository: TourRepository

    @Autowired
    private lateinit var ratingRepository: LocationRatingRepository

    @Autowired
    private lateinit var checkInRepository: CheckInRepository

    @Autowired
    private lateinit var imageRepository: ImageRepository

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var imageService: ImageService

    fun findByEmail(email: String): User {
        if (userRepository.findByEmail(email).isPresent) return userRepository.findByEmail(email).get()
        throw GenericException("User with email = $email was not found in the database")
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun findById(id: Int): User {
        val user: Optional<User> = userRepository.findById(id)
        if (user.isEmpty) throw GenericException("User with id = $id was not found in the database")
        return user.get()
    }

    fun checkUsername(nameResource: String, nameUser: String, container: ExceptionContainer) {
        if (nameResource.length < 3) {
            container.addException(PropertyException("username", "Should be longer than 3 characters"))
        }
        if (nameResource.length > 30) {
            container.addException(PropertyException("username", "Should be shorter than 30 characters"))
        }
        userRepository.findByUsernameIgnoreCase(nameResource).ifPresent {
            if (it.username != nameUser) {
                container.addException(PropertyException("username", "This username is already in use."))
            }
        }
    }

    fun update(user: User, resource: UserWrapper) {
        val container = ExceptionContainer()

        resource.email.ifPresent {
            if (authService.checkEmail(resource.email.get()).not())
                container.addException(PropertyException("email", "The email is not valid"))
            checkEmailUnique(user.email, resource.email.get(), container)
        }

        resource.username.ifPresent { checkUsername(resource.username.get(), user.username, container) }

        container.throwIfNotEmpty()

        resource.email.ifPresent { user.email = resource.email.get() }
        resource.avatarId.ifPresent {
            imageRepository.findById(resource.avatarId.get()).ifPresentOrElse({
                user.avatar = it
            }, {
                if (user.avatar != null) {
                    val id = user.avatar!!.id
                    user.avatar = null
                    imageRepository.deleteById(id)
                    imageRepository.flush()
                }
            })
        }
        resource.username.ifPresent { user.username = resource.username.get() }
        userRepository.saveAndFlush(user)
    }


    private fun checkEmailUnique(emailUser: String, emailResource: String, container: ExceptionContainer) {
        userRepository.findByEmail(emailResource).ifPresent {
            if (it.email != emailUser) {
                container.addException(PropertyException("email", "Email already exists, try again."))
            }
        }
    }

    fun deleteUser(user: User) {
        userRepository.delete(user)
        //TODO check of echt alles van die user mee verwijderd wordt.
        userRepository.flush()
    }

    fun <T, U> apply(x: T, f: (T) -> U): U {
        return f(x)
    }

    fun deleteData(user: User, resource: DeleteWrappper) {
        resource.type.ifPresentOrElse({ dataType ->
            when (dataType) {
                DATATYPE.COMMENTS -> apply(user, {
                    commentRepository.deleteAll(commentRepository.findAllByCreator(it))
                })
                DATATYPE.RATINGS -> apply(user, {
                    ratingRepository.deleteAll(ratingRepository.findAllByCreator(it))
                })
                DATATYPE.LOCATIONS -> apply(user, {
                    locationRepository.deleteAll(locationRepository.findByCreator(it))
                })
                DATATYPE.TOURS -> apply(user, {
                    tourRepository.deleteAll(tourRepository.getAllByCreator(it))
                })
                DATATYPE.VISITS -> apply(user, {
                    checkInRepository.deleteAll(checkInRepository.findAllByCreator(it))
                })
                else -> throw PropertyException("type", "The given type does not exist.")
            }
        }, {
            throw PropertyException("type", "the type is an expected value")
        })
    }

    fun privacy(resource: PrivacyWrapper, user: User) {
        resource.displayOnLeaderboards.ifPresent {
            user.displayOnLeaderboards = it
            userRepository.saveAndFlush(user)
        }
    }

}
