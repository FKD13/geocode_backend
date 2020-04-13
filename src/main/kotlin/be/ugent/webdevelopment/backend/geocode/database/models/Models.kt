package be.ugent.webdevelopment.backend.geocode.database.models

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldId
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldType
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "users")
@JsonldType("https://schema.org/Person")
@JsonldId("users")
class User constructor(
        @field:JsonldId
        @Id @GeneratedValue
        @JsonView(View.PublicDetail::class)
        var id: Int = 0,

        @field:JsonldProperty("https://schema.org/email")
        @Column(nullable = false, unique = true, length = 512)
        @JsonView(View.PublicDetail::class)
        var email: String = "",

        @field:JsonldProperty("https://schema.org/alternateName")
        @Column(nullable = false, unique = true)
        @JsonView(View.PublicDetail::class)
        var username: String = "",

        @field:JsonldProperty("https://schema.org/image")
        @Column(nullable = false, name = "avatar_url")
        @JsonView(View.PublicDetail::class)
        var avatarUrl: String = "",

        @Column(nullable = false)
        @JsonView(View.PrivateDetail::class)
        var admin: Boolean = false,

        @Column(nullable = false)
        @JsonView(View.PrivateDetail::class)
        var time: Date = Date(),

        @JsonIgnore
        @Column(nullable = false) var password: String = "",

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var locations: Set<Location> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var tours: Set<Tour> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var comments: Set<Comment> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var location_ratings: Set<LocationRating> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var check_ins: Set<CheckIn> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var reports: Set<Report> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.LAZY)
        var user_tours: Set<UserTour> = Collections.emptySet()
)

@Entity
@Table(name = "locations")
@JsonldType("https://schema.org/Place")
@JsonldId("locations")
class Location constructor(
        @JsonIgnore
        @Id @GeneratedValue var id: Int = 0,

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/longitude")
        @JsonView(View.PublicDetail::class)
        var longitude: Double = 0.0,

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/latitude")
        @JsonView(View.PublicDetail::class)
        var latitude: Double = 0.0,

        @Column(nullable = false, name = "secret_id", unique = true)
        @field:JsonldId
        @JsonView(View.PublicDetail::class)
        var secretId: String = "",

        @Column(nullable = false)
        @JsonView(View.PrivateDetail::class)
        var time: Date = Date(),

        @Column(nullable = false)
        @JsonView(View.PublicDetail::class)
        var listed: Boolean = false,

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/name")
        @JsonView(View.PublicDetail::class)
        var name: String = "",

        @Column(nullable = false, length = 2048)
        @field:JsonldProperty("https://schema.org/description")
        @JsonView(View.PublicDetail::class)
        var description: String = "",

        @ManyToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY, optional = false)
        @field:JsonldProperty("https://schema.org/Person")
        @JsonView(View.PublicDetail::class)
        var creator: User = User(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location") var comments: Set<Comment> = Collections.emptySet(),
        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location") var location_ratings: Set<LocationRating> = Collections.emptySet(),
        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location") var check_ins: Set<CheckIn> = Collections.emptySet(),
        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location") var reports: Set<Report> = Collections.emptySet(),
        @JsonIgnore
        @ManyToMany(cascade = [CascadeType.PERSIST]) var tours: Set<Tour> = Collections.emptySet()
)

@Entity
@Table(name = "tours")
class Tour(
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToMany(cascade = [CascadeType.PERSIST]) var locations: Set<Location> = Collections.emptySet(),
        @Column(nullable = false) var name: String = "",
        @Column(length = 2048, nullable = false) var description: String = "",
        @Column(nullable = false) var time: Date = Date(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "tour") var user_tours: Set<UserTour> = Collections.emptySet()
)

@Entity
@Table(name = "comments")
class Comment(
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var time: Date = Date(),
        @Column(nullable = false, length = 1024) var comment: String = ""
)

@Entity
@Table(name = "location_ratings")
class LocationRating(
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var rating: Int = 0
)

@Entity
@Table(name = "check_ins")
class CheckIn(
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var time: Date = Date()
)

@Entity
@Table(name = "reports")
class Report(
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var time: Date = Date(),
        @Column(nullable = false) var reason: String = "",
        @Column(nullable = false) var resolved: Boolean = false,
        @Column(nullable = false, name = "image_url", length = 1024) var imageUrl: String = ""
)

@Entity
@Table(name = "user_tours")
class UserTour(
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(optional = false, cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY) var user: User = User(),
        @ManyToOne(optional = false, cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY) var tour: Tour = Tour(),
        @Column(nullable = false) var completed: Boolean = false
)

