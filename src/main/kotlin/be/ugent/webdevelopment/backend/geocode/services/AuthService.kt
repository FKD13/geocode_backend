package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ResetWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserLoginWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserRegisterWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress

@Service
class AuthService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var captchaService: CaptchaService

    private val passwordPattern = Pattern.compile("^.*['\"`´].*$")
    private val usernamePattern = Pattern.compile("^[^ ][A-Za-z0-9 \\-_]+[^ ]$")

    private val bCryptRounds = 12

    fun checkUser(username: String): Boolean {
        val user: Optional<User> = userRepository.findByUsernameIgnoreCase(username)
        return !user.isEmpty
    }

    fun checkEmail(email: String): Boolean {
        try {
            InternetAddress(email).validate()
        } catch (e: AddressException) {
            return false
        }
        return true
    }

    fun tryLogin(resource: UserLoginWrapper) {
        val user = userRepository.findByEmail(resource.email)

        if (user.isEmpty || BCrypt.checkpw(resource.password, user.get().password).not()) {
            val exc = ExceptionContainer(code = HttpStatus.BAD_REQUEST)
            exc.addException(GenericException("Unable to login."))
            exc.addException(PropertyException("email", "Email and/or password is wrong."))
            exc.addException(PropertyException("password", "Email and/or password is wrong."))
            throw exc
        }
    }

    fun checkPasswordLength(password: String, container: ExceptionContainer){
        if(password.length < 8){
            container.addException(PropertyException("password", "Should be at least 8 characters"))
        }
        if (password.length > 64){
            container.addException(PropertyException("password", "Should be at most 64 characters"))
        }
    }

    fun checkPasswordRepeat(password: String, passwordRepeat: String?, container: ExceptionContainer){
        if (password != passwordRepeat){
            container.addException(PropertyException("passwordRepeat", "Passwords didn't match, try again."))
        }
    }

    private fun checkPasswordSpecialChars(password: String, container: ExceptionContainer) {
        if (passwordPattern.matcher(password).matches()){
            container.addException(PropertyException("password", "Should not contain ` ´ ' or \""))
        }
    }

    fun tryRegister(resource: UserRegisterWrapper) {
        val exc = ExceptionContainer(code = HttpStatus.BAD_REQUEST)
        if (resource.username.length < 3) {
            exc.addException(PropertyException("username", "Should be at least 3 characters"))
        }

        if (resource.username.length > 30) {
            exc.addException(PropertyException("username", "Should be at most 30 characters"))
        }

        if (!usernamePattern.matcher(resource.username).matches()) {
            exc.addException(PropertyException("username", "You can use letters, numbers, spaces, underscores & hyphens, but it can't begin or end with a space."))
        }

        if (resource.email.length > 320) {
            exc.addException(PropertyException("email", "Should be at most 320 characters"))
        }

        if (!checkEmail(resource.email)) {
            exc.addException(PropertyException("email", "Invalid email adress"))
        }

        checkPasswordRepeat(resource.password, resource.passwordRepeat, exc)

        checkPasswordLength(resource.password, exc)

        checkPasswordSpecialChars(resource.password, exc)

        if (resource.captcha.isEmpty) {
            exc.addException(GenericException("Empty captcha, try again."))
        } else {
            try {
                captchaService.validateCaptcha(resource.captcha.get())
            } catch (e: GenericException) {
                exc.addException(e)
            }
        }

        val existingUser = userRepository.findByEmailOrUsernameIgnoreCase(resource.email, resource.username)
        if (existingUser.isPresent) {
            val user = existingUser.get()
            if (user.username.toLowerCase() == resource.username.toLowerCase()) {
                exc.addException(PropertyException("username", "Username already exists, try again."))
            }

            if (user.email.toLowerCase() == resource.email.toLowerCase()) {
                exc.addException(PropertyException("email", "Email already exists, try again."))
            }
        }

        if (exc.isEmpty().not()) {
            exc.addException(GenericException("Unable to register."))
            throw exc
        }

        val hash = BCrypt.hashpw(resource.password, BCrypt.gensalt(bCryptRounds))

        userRepository.saveAndFlush(User(
                email = resource.email,
                username = resource.username,
                password = hash))

    }



    fun passwordReset(resource: ResetWrapper, user: User) {
        val container = ExceptionContainer()

        resource.password.ifPresentOrElse({ password ->
            resource.passwordRepeat.ifPresentOrElse({ passwordRepeat ->
                checkPasswordRepeat(password, passwordRepeat, container)
            }, {
                container.addException(PropertyException("newPasswordRepeat", "The repeated new password is an expected value."))
            })
            checkPasswordLength(password, container)
            checkPasswordSpecialChars(password, container)
        },{
            container.addException(PropertyException("newPassword", "The new password is an expected value."))
        })
        container.ifNotEmpty {
            container.addException(GenericException("The password could not be changed."))
            throw container
        }
        val hash = BCrypt.hashpw(resource.password.get(), BCrypt.gensalt(bCryptRounds))
        user.password = hash
        userRepository.saveAndFlush(user)
    }
}
