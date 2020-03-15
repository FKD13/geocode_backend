package be.ugent.webdevelopment.backend.geocode.database.models

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "users")
class User (
        @Id @GeneratedValue var id: Int = 0,
        @Column(nullable = false, unique = true, length = 512) var email: String = "",
        @Column(nullable = false, unique = true) var username: String = "",
        @Column(name = "avatar_url") var avatarUrl: String? = "",
        @Column(nullable = false) var admin: Boolean = false,
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false) var password: String = ""
)

@Entity
@Table(name = "locations")
class Location (
        @Id @GeneratedValue var id: Int = 0,
        @Column(nullable = false) var longitude: Double = 0.0,
        @Column(nullable = false) var latitude: Double = 0.0,
        @Column(nullable = false, name = "secret_id", unique = true) var secretId: String = "",
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false) var listed: Boolean = false,
        @Column(nullable = false) var name: String = "",
        @Column(nullable = false, length = 2048) var description: String = "",
        @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = false) var creator: User = User()
)

@Entity
@Table(name = "tours")
class Tour (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToMany(cascade = [CascadeType.ALL]) var locations: List<Location>? = null,
        @Column(nullable = false) var name: String = "",
        @Column(length = 2048, nullable = false) var description: String = "",
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now()
)

@Entity
@Table(name = "comments")
class Comment (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false, length = 1024) var comment: String = ""
)

@Entity
@Table(name = "location_ratings")
class LocationRating (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var rating: Int = 0
)

@Entity
@Table(name = "check_ins")
class CheckIn (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now()
)

@Entity
@Table(name = "reports")
class Report (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false) var reason: String = "",
        @Column(nullable = false) var resolved: Boolean = false,
        @Column(nullable = false, name = "image_url", length = 1024) var imageUrl: String = ""
)

@Entity
@Table(name = "user_tours")
class UserTour (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(optional = false, cascade = [CascadeType.ALL], fetch = FetchType.LAZY) var user: User = User(),
        @ManyToOne(optional = false, cascade = [CascadeType.ALL], fetch = FetchType.LAZY) var tour: Tour = Tour(),
        @Column(nullable = false) var completed: Boolean = false
)
