package be.ugent.webdevelopment.backend.geocode.database.models

import be.ugent.webdevelopment.backend.geocode.database.View
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import cz.cvut.kbss.jopa.model.annotations.OWLClass
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty
import cz.cvut.kbss.jopa.model.annotations.Properties
import java.net.URI
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "users")
@OWLClass(iri = "https://schema.org/Person")
class User (
        @cz.cvut.kbss.jopa.model.annotations.Id
        @Id @GeneratedValue
        var id: Int = 0,
        @Column(nullable = false, unique = true, length = 512) var email: String = "",
        @Column(nullable = false, unique = true) var username: String = "",
        @Column(nullable = false, name = "avatar_url") var avatarUrl: String = "",
        @Column(nullable = false) var admin: Boolean = false,
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false) var password: String = "",

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY) var locations: Set<Location> = Collections.emptySet(),
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY) var tours: Set<Tour> = Collections.emptySet(),
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY) var comments: Set<Comment> = Collections.emptySet(),
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY) var location_ratings: Set<LocationRating> = Collections.emptySet(),
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY) var check_ins: Set<CheckIn> = Collections.emptySet(),
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY) var reports: Set<Report> = Collections.emptySet(),
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.LAZY) var user_tours: Set<UserTour> = Collections.emptySet()
)

@Entity
@Table(name = "locations")
@OWLClass(iri = "https://schema.org/Place")
class Location (

        @Id @GeneratedValue var id: Int = 0,
        @OWLDataProperty(iri = "https://schema.org/Place#longitude")
        @Column(nullable = false)
        @JsonView(View.PublicDetail::class)
        @JsonAlias("longitude")
        var longitude: Double = 0.0,

        @OWLDataProperty(iri = "https://schema.org/Place#latitude")
        @Column(nullable = false)
        @JsonView(View.PublicDetail::class)
        @JsonAlias("latitude")
        var latitude: Double = 0.0,

        @cz.cvut.kbss.jopa.model.annotations.Id
        @Column(nullable = false, name = "secret_id", unique = true)
        @JsonView(View.PublicDetail::class)
        @JsonAlias("secretId")
        var secretId: String = "",

        @Column(nullable = false)
        @JsonView(View.PublicDetail::class)
        @JsonAlias("time")
        var time: LocalDateTime = LocalDateTime.now(),

        @Column(nullable = false)
        @JsonView(View.PublicDetail::class)
        @JsonAlias("listed")
        var listed: Boolean = false,

        @OWLDataProperty(iri = "https://schema.org/Place#name")
        @JsonView(View.PublicDetail::class)
        @Column(nullable = false)
        @JsonAlias("name")
        var name: String = "",

        @OWLDataProperty(iri = "https://schema.org/Place#description")
        @Column(nullable = false, length = 2048)
        @JsonAlias("description")
        var description: String = "",

        @OWLObjectProperty(iri = "https://schema.org/Person")
        @JsonView(View.PublicDetail::class)
        @ManyToOne(cascade = [CascadeType.PERSIST] ,fetch = FetchType.LAZY, optional = false)
        @JsonAlias("creator")
        var creator: User = User(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location") var comments: Set<Comment> = Collections.emptySet(),
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location") var location_ratings: Set<LocationRating> = Collections.emptySet(),
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location") var check_ins: Set<CheckIn> = Collections.emptySet(),
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location") var reports: Set<Report> = Collections.emptySet(),
        @ManyToMany(cascade = [CascadeType.PERSIST]) var tours: Set<Tour> = Collections.emptySet()
)

@Entity
@Table(name = "tours")
class Tour (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToMany(cascade = [CascadeType.PERSIST]) var locations: Set<Location> = Collections.emptySet(),
        @Column(nullable = false) var name: String = "",
        @Column(length = 2048, nullable = false) var description: String = "",
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "tour") var user_tours: Set<UserTour> = Collections.emptySet()
)

@Entity
@Table(name = "comments")
class Comment (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false, length = 1024) var comment: String = ""
)

@Entity
@Table(name = "location_ratings")
class LocationRating (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var rating: Int = 0
)

@Entity
@Table(name = "check_ins")
class CheckIn (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now()
)

@Entity
@Table(name = "reports")
class Report (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var creator: User = User(),
        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY) var location: Location = Location(),
        @Column(nullable = false) var time: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false) var reason: String = "",
        @Column(nullable = false) var resolved: Boolean = false,
        @Column(nullable = false, name = "image_url", length = 1024) var imageUrl: String = ""
)

@Entity
@Table(name = "user_tours")
class UserTour (
        @Id @GeneratedValue var id: Int = 0,
        @ManyToOne(optional = false, cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY) var user: User = User(),
        @ManyToOne(optional = false, cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY) var tour: Tour = Tour(),
        @Column(nullable = false) var completed: Boolean = false
)