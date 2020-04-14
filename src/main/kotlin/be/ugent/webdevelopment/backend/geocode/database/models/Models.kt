package be.ugent.webdevelopment.backend.geocode.database.models

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldId
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldType
import be.ugent.webdevelopment.backend.geocode.jsonld.internal.JsonldResourceSerializer
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*
import javax.persistence.*

@JsonSerialize(using = JsonldResourceSerializer::class)
abstract class JsonLDSerializable

@Entity
@Table(name = "users")
@JsonldType("https://schema.org/Person")
@JsonldId("users")
class User constructor(

        @field:JsonldId
        @Id @GeneratedValue
        @field:JsonView(View.PublicDetail::class)
        var id: Int = 0,

        @field:JsonldProperty("https://schema.org/email")
        @Column(nullable = false, unique = true, length = 512)
        @field:JsonView(View.PublicDetail::class)
        var email: String = "",

        @field:JsonldProperty("https://schema.org/alternateName")
        @Column(nullable = false, unique = true)
        @field:JsonView(View.PublicDetail::class)
        var username: String = "",

        @field:JsonldProperty("https://schema.org/image")
        @Column(nullable = false, name = "avatar_url")
        @field:JsonView(View.PublicDetail::class)
        var avatarUrl: String = "",

        @Column(nullable = false)
        @field:JsonView(View.PrivateDetail::class)
        var admin: Boolean = false,

        @Column(nullable = false)
        @field:JsonView(View.PrivateDetail::class)
        var time: Date = Date(),

        @JsonIgnore
        @Column(nullable = false)
        var password: String = "",

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
) : JsonLDSerializable()

@Entity
@Table(name = "locations")
@JsonldType("https://schema.org/Place")
@JsonldId("locations")
class Location constructor(

        @Id
        @JsonIgnore
        @GeneratedValue
        var id: Int = 0,

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/longitude")
        @field:JsonView(View.PublicDetail::class)
        var longitude: Double = 0.0,

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/latitude")
        @field:JsonView(View.PublicDetail::class)
        var latitude: Double = 0.0,

        @Column(nullable = false, name = "secret_id", unique = true)
        @field:JsonldId
        @field:JsonView(View.PublicDetail::class)
        var secretId: String = "",

        @Column(nullable = false)
        @field:JsonView(View.PrivateDetail::class)
        var time: Date = Date(),

        @Column(nullable = false)
        @field:JsonView(View.PublicDetail::class)
        var listed: Boolean = false,

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/name")
        @field:JsonView(View.PublicDetail::class)
        var name: String = "",

        @Column(nullable = false, length = 2048)
        @field:JsonldProperty("https://schema.org/description")
        @field:JsonView(View.PublicDetail::class)
        var description: String = "",

        @ManyToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY, optional = false)
        @field:JsonldProperty("https://schema.org/Person")
        @field:JsonView(View.PublicDetail::class)
        var creator: User = User(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location")
        var comments: Set<Comment> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location")
        var location_ratings: Set<LocationRating> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location")
        var check_ins: Set<CheckIn> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "location")
        var reports: Set<Report> = Collections.emptySet(),

        @JsonIgnore
        @ManyToMany(cascade = [CascadeType.PERSIST])
        var tours: Set<Tour> = Collections.emptySet()
) : JsonLDSerializable()


@Entity
@Table(name = "tours")
@JsonldType("https://schema.org/CreativeWork") //todo miss https://schema.org/Guide van maken
@JsonldId("tours") //todo check of dit klopt met de endpoints
class Tour(
        @Id
        @GeneratedValue
        @field:JsonldId
        @JsonView(View.PublicDetail::class)
        var id: Int = 0,

        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/CreativeWork#creator")
        @JsonView(View.PublicDetail::class)
        var creator: User = User(),

        @ManyToMany(cascade = [CascadeType.PERSIST])
        @field:JsonldProperty("https://schema.org/CreativeWork#about")
        @JsonView(View.PublicDetail::class)
        var locations: Set<Location> = Collections.emptySet(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Thing#name")
        @JsonView(View.PublicDetail::class)
        var name: String = "",

        @Column(length = 2048, nullable = false)
        @field:JsonldProperty("https://schema.org/CreativeWork#abstract")
        @JsonView(View.PublicDetail::class)
        var description: String = "",

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/CreativeWork#dateCreated")
        @JsonView(View.PrivateDetail::class)
        var time: Date = Date(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "tour")
        var user_tours: Set<UserTour> = Collections.emptySet()
) : JsonLDSerializable()

@Entity
@Table(name = "comments")
@JsonldType("https://schema.org/Comment")
@JsonldId("comments") //todo check of dit klopt met de endpoints
class Comment(
        @Id
        @GeneratedValue
        @field:JsonldId
        @JsonView(View.PublicDetail::class)
        var id: Int = 0,

        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Comment#creator")
        @JsonView(View.PublicDetail::class)
        var creator: User = User(),

        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Comment#about")
        @JsonView(View.PublicDetail::class)
        var location: Location = Location(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Comment#dateCreated")
        @JsonView(View.PrivateDetail::class)
        var time: Date = Date(),

        @Column(nullable = false, length = 1024)
        @field:JsonldProperty("https://schema.org/Comment#text")
        @JsonView(View.PublicDetail::class)
        var comment: String = ""
) : JsonLDSerializable()

@Entity
@Table(name = "location_ratings")
@JsonldType("https://schema.org/AggregateRating")
@JsonldId("ratings") //todo check of dit klopt met de endpoints
class LocationRating(
        @Id
        @GeneratedValue
        @field:JsonldId
        @JsonView(View.PublicDetail::class)
        var id: Int = 0,

        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Rating#author")
        @JsonView(View.PublicDetail::class)
        var creator: User = User(),

        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Rating#about")
        @JsonView(View.PublicDetail::class)
        var location: Location = Location(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Rating#ratingValue")
        @JsonView(View.PublicDetail::class)
        var rating: Int = 0
) : JsonLDSerializable()

@Entity
@Table(name = "check_ins")
@JsonldType("https://schema.org/DiscoverAction")
@JsonldId("checkIn") //todo check of dit klopt met de endpoints
class CheckIn(
        @Id
        @GeneratedValue
        @JsonldId
        @JsonView(View.PublicDetail::class)
        var id: Int = 0,

        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/DiscoverAction#agent")
        @JsonView(View.PublicDetail::class)
        var creator: User = User(),

        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/DiscoverAction#location")
        @JsonView(View.PublicDetail::class)
        var location: Location = Location(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/DiscoverAction#location#endTime")
        @JsonView(View.PrivateDetail::class)
        var time: Date = Date()
) : JsonLDSerializable()

@Entity
@Table(name = "reports")
@JsonldType("https://schema.org/Review")
@JsonldId("reports") //todo check of dit klopt met de endpoints
class Report(
        @Id
        @GeneratedValue
        @field:JsonldId
        @JsonView(View.AdminDetail::class)
        var id: Int = 0,

        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Review#creator")
        @JsonView(View.AdminDetail::class)
        var creator: User = User(),

        @ManyToOne(cascade = [CascadeType.PERSIST], optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Review#itemReviewed")
        @JsonView(View.AdminDetail::class)
        var location: Location = Location(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Review#dateCreated")
        @JsonView(View.AdminDetail::class)
        var time: Date = Date(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Review#reviewBody")
        @JsonView(View.AdminDetail::class)
        var reason: String = "",

        @Column(nullable = false)
        @JsonView(View.AdminDetail::class)
        var resolved: Boolean = false,

        @Column(nullable = false, name = "image_url", length = 1024)
        @field:JsonldProperty("https://schema.org/Review#image")
        @JsonView(View.AdminDetail::class)
        var imageUrl: String = ""
) : JsonLDSerializable()

@Entity
@Table(name = "user_tours")
@JsonldType("https://schema.org/Action")
class UserTour(
        @Id
        @GeneratedValue
        @field:JsonldId
        @JsonView(View.PublicDetail::class)
        var id: Int = 0,

        @ManyToOne(optional = false, cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Action#agent")
        @JsonView(View.PublicDetail::class)
        var user: User = User(),

        @ManyToOne(optional = false, cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Action#object")
        @JsonView(View.PublicDetail::class)
        var tour: Tour = Tour(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Action#actionStatus")
        @JsonView(View.PublicDetail::class)
        var completed: Boolean = false
) : JsonLDSerializable()

