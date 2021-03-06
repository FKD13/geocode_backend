package be.ugent.webdevelopment.backend.geocode.database.models

import be.ugent.webdevelopment.backend.geocode.achievements.TypeAchievement
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldId
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldType
import be.ugent.webdevelopment.backend.geocode.jsonld.internal.JsonldResourceSerializer
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*
import javax.persistence.*

@JsonSerialize(using = JsonldResourceSerializer::class)
interface JsonLDSerializable

@Entity
@Table(name = "users")
@JsonldType("https://schema.org/Person")
@JsonldId("users")
class User constructor(

        @field:JsonldId
        @Id @GeneratedValue
        @field:JsonView(View.Id::class)
        var id: Int = 0,

        @field:JsonldProperty("https://schema.org/Person#email")
        @Column(nullable = false, unique = true, length = 512)
        @field:JsonView(View.PrivateDetail::class)
        var email: String = "",

        @field:JsonldProperty("https://schema.org/Person#alternateName")
        @Column(nullable = false, unique = true)
        @field:JsonView(View.List::class)
        var username: String = "",

        @field:JsonldProperty("https://schema.org/Person#image")
        @OneToOne(optional = true, cascade = [CascadeType.ALL])
        @field:JsonView(View.List::class)
        var avatar: Image? = null,

        @Column(nullable = false)
        @field:JsonView(View.PrivateDetail::class)
        var admin: Boolean = false,

        @Column(nullable = false)
        @field:JsonView(View.PrivateDetail::class)
        var createdAt: Date = Date(),

        @JsonIgnore
        @Column(nullable = false)
        var password: String = "",

        @JsonIgnore
        @Column(nullable = false)
        var displayOnLeaderboards: Boolean = true,

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var locations: MutableSet<Location> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var tours: MutableSet<Tour> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var comments: MutableSet<Comment> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var location_ratings: MutableSet<LocationRating> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var check_ins: MutableSet<CheckIn> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var reports: MutableSet<Report> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.LAZY)
        var user_tours: Set<UserTour> = Collections.emptySet(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.LAZY)
        var achievement_user: Set<AchievementUser> = Collections.emptySet()

) : JsonLDSerializable

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
        @field:JsonldProperty("https://schema.org/Place#longitude")
        @field:JsonView(View.List::class)
        var longitude: Double = 0.0,

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Place#latitude")
        @field:JsonView(View.List::class)
        var latitude: Double = 0.0,

        @Column(nullable = false, name = "secret_id", unique = true)
        @field:JsonldId
        @field:JsonView(View.Id::class)
        var secretId: String = "",

        @Column(nullable = false)
        @field:JsonView(View.PublicDetail::class)
        var createdAt: Date = Date(),

        @Column(nullable = false)
        @field:JsonView(View.PrivateDetail::class, View.AdminDetail::class)
        var listed: Boolean = false,

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Place#name")
        @field:JsonView(View.List::class)
        var name: String = "",

        @Column(nullable = false, length = 2048)
        @field:JsonldProperty("https://schema.org/Place#description")
        @field:JsonView(View.PublicDetail::class)
        var description: String = "",

        @Column(nullable = false)
        @field:JsonView(View.AdminDetail::class)
        var visitSecret: String = "",

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @field:JsonldProperty("https://schema.org/CreativeWork#creator")
        @field:JsonView(View.List::class)
        var creator: User = User(),

        @Column(nullable = false, length = 128)
        @field:JsonldProperty("https://schema.org/GeoCoordinates#addressCountry")
        @field:JsonView(View.PublicDetail::class)
        var country: String = "",

        @Column(nullable = false, length = 512)
        @field:JsonldProperty("https://schema.org/GeoCoordinates#address")
        @field:JsonView(View.PublicDetail::class)
        var address: String = "",

        @Column(nullable = false)
        @field:JsonView(View.List::class, View.AdminDetail::class)
        var active: Boolean = false,

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

) : JsonLDSerializable


@Entity
@Table(name = "tours")
@JsonldType("https://schema.org/CreativeWork")
@JsonldId("tours")
class Tour constructor( //todo check al de JsonViews als we dit gaan implementeren

        @Id
        @GeneratedValue
        @field:JsonldId
        @field:JsonView(View.Id::class)
        var id: Int = 0,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/CreativeWork#creator")
        @field:JsonView(View.List::class)
        var creator: User = User(),

        @ManyToMany(fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/CreativeWork#about")
        @field:JsonView(View.PublicDetail::class)
        var locations: List<Location> = Collections.emptyList(),

        @Column(nullable = false, name = "secret_id", unique = true)
        @field:JsonldId
        @field:JsonView(View.Id::class)
        var secretId: String = "",

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/CreativeWork#name")
        @field:JsonView(View.List::class)
        var name: String = "",

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/TravelAction#distance")
        @field:JsonView(View.List::class)
        var totalDistance: Double = 0.0,

        @Column(length = 2048, nullable = false)
        @field:JsonldProperty("https://schema.org/CreativeWork#abstract")
        @field:JsonView(View.PublicDetail::class)
        var description: String = "",

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/CreativeWork#dateCreated")
        @field:JsonView(View.PublicDetail::class)
        var createdAt: Date = Date(),

        @Column(nullable = false)
        @field:JsonView(View.PrivateDetail::class, View.AdminDetail::class)
        var listed: Boolean = false,

        @Column(nullable = false)
        @field:JsonView(View.List::class, View.AdminDetail::class)
        var active: Boolean = false,

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "tour")
        var user_tours: Set<UserTour> = Collections.emptySet()

) : JsonLDSerializable

@Entity
@Table(name = "comments")
@JsonldType("https://schema.org/Comment")
@JsonldId("comments")
class Comment constructor(

        @Id
        @GeneratedValue
        @field:JsonldId
        @field:JsonView(View.Id::class)
        var id: Int = 0,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Comment#creator")
        @field:JsonView(View.List::class)
        var creator: User = User(),

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Comment#about")
        @field:JsonView(View.List::class)
        var location: Location = Location(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Comment#dateCreated")
        @field:JsonView(View.List::class)
        var createdAt: Date = Date(),

        @Column(nullable = false, length = 1024)
        @field:JsonldProperty("https://schema.org/Comment#text")
        @field:JsonView(View.List::class)
        @field:JsonProperty("message")
        var comment: String = ""

) : JsonLDSerializable

@Entity
@Table(name = "location_ratings")
@JsonldType("https://schema.org/AggregateRating")
@JsonldId("ratings")
class LocationRating constructor(

        @Id
        @GeneratedValue
        @field:JsonldId
        @field:JsonView(View.Id::class)
        var id: Int = 0,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Rating#author")
        @field:JsonView(View.List::class)
        var creator: User = User(),

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Rating#about")
        @field:JsonView(View.List::class)
        var location: Location = Location(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Rating#ratingValue")
        @field:JsonView(View.List::class)
        var rating: Int = 0,

        @Column(nullable = false, length = 1024)
        @field:JsonldProperty("https://schema.org/Rating#ratingExplanation")
        @field:JsonView(View.List::class)
        var message: String = ""

) : JsonLDSerializable

@Entity
@Table(name = "check_ins")
@JsonldType("https://schema.org/DiscoverAction")
@JsonldId("checkin")
class CheckIn constructor(

        @Id
        @GeneratedValue
        @JsonldId
        @field:JsonView(View.Id::class)
        var id: Int = 0,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/DiscoverAction#agent")
        @field:JsonView(View.List::class)
        var creator: User = User(),

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/DiscoverAction#location")
        @field:JsonView(View.List::class)
        var location: Location = Location(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/DiscoverAction#endTime")
        @field:JsonView(View.PrivateDetail::class)
        var createdAt: Date = Date()

) : JsonLDSerializable

@Entity
@Table(name = "reports")
@JsonldType("https://schema.org/Review")
@JsonldId("reports")
class Report constructor(

        @Id
        @GeneratedValue
        @field:JsonldId
        @field:JsonView(View.AdminDetail::class)
        var id: Int = 0,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Review#creator")
        @field:JsonView(View.AdminDetail::class)
        var creator: User = User(),

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Review#itemReviewed")
        @field:JsonView(View.AdminDetail::class)
        var location: Location = Location(),

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Review#dateCreated")
        @field:JsonView(View.AdminDetail::class)
        var createdAt: Date = Date(),

        @Column(nullable = false, length = 2048)
        @field:JsonldProperty("https://schema.org/Review#reviewBody")
        @field:JsonView(View.AdminDetail::class)
        var message: String = "",

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Review#reviewAspect")
        @field:JsonView(View.AdminDetail::class)
        var reason: String = "",

        @Column(nullable = false)
        @field:JsonView(View.AdminDetail::class)
        var resolved: Boolean = false,

        @OneToOne(optional = true, cascade = [CascadeType.ALL])
        @field:JsonldProperty("https://schema.org/Review#image")
        @field:JsonView(View.AdminDetail::class)
        var image: Image? = null

) : JsonLDSerializable

@Entity
@Table(name = "user_tours")
@JsonldType("https://schema.org/Action")
class UserTour constructor(

        @Id
        @GeneratedValue
        @field:JsonldId
        @field:JsonView(View.Id::class)
        var id: Int = 0,

        @ManyToOne(optional = false, cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Action#agent")
        @field:JsonView(View.List::class)
        var user: User = User(),

        @ManyToOne(optional = false, cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
        @field:JsonldProperty("https://schema.org/Action#object")
        @field:JsonView(View.List::class)
        var tour: Tour = Tour(),

        @Column(nullable = false)
        @JsonIgnore
        var amountLocationsVisited: Int = 1,

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/Action#actionStatus")
        @field:JsonView(View.PrivateDetail::class, View.AdminDetail::class)
        var completed: Boolean = false,

        @Column(nullable = false)
        @field:JsonView(View.PrivateDetail::class)
        var createdAt: Date = Date()

) : JsonLDSerializable

@Entity
@Table(name = "images")
@JsonldType("https://schema.org/image")
@JsonldId("images")
class Image constructor(
        @Id
        @GeneratedValue
        @field:JsonldId
        @field:JsonView(View.Id::class)
        var id: Int = 0,

        @Lob
        @Column(nullable = false)
        @JsonIgnore
        var image: Array<Byte> = arrayOf(0),

        @Column(nullable = false)
        @JsonIgnore
        var contentType: String = "",

        @Column(nullable = true)
        @JsonIgnore
        var resourcePath: String? = null

) : JsonLDSerializable

/**
 * A Entity to map achievements to users.
 */
@Entity
@Table
class AchievementUser(

        @Id
        @GeneratedValue
        var id: Int = 0,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        var user: User = User(),

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        var achievement: Achievement = Achievement(),

        @JsonIgnore
        var achievedAt: Date = Date()

)

@Entity
@Table(name = "achievements")
@JsonldType("https://schema.org/CreativeWork")
@JsonldId("achievements")
class Achievement constructor(

        @Id
        @GeneratedValue
        @field:JsonldId
        @field:JsonView(View.Id::class)
        var id: Int = 0,

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/CreativeWork#name")
        @field:JsonView(View.List::class)
        private val name: String = "",

        @Column(nullable = false)
        @field:JsonldProperty("https://schema.org/CreativeWork#abstract")
        @field:JsonView(View.List::class)
        private val description: String = "",

        @ManyToOne(optional = true)
        @field:JsonldProperty("https://schema.org/CreativeWork#image")
        @field:JsonView(View.List::class)
        var image: Image = Image(),

        @JsonIgnore
        @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "achievement")
        var achievement_user: Set<AchievementUser> = Collections.emptySet(),

        @Column(nullable = false)
        @JsonIgnore
        var type: TypeAchievement = TypeAchievement.COUNTRY,

        /**
         * Value to exceed in Count Achievement.
         */
        @JsonIgnore
        var value: Int? = null,

        /**
         * stringValue to match, for County Achievement.
         */
        @JsonIgnore
        var stringValue: String? = null

) : JsonLDSerializable
