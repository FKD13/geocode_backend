package be.ugent.webdevelopment.backend.geocode.dao

import be.ugent.webdevelopment.backend.geocode.model.User
import org.springframework.stereotype.Repository

@Repository("fakeUserDao")
class FakeUserDataAccessService : UserDao{

    private var db: ArrayList<User> = ArrayList()

    override fun insertUser(id: Long, user: User): Long {
        db.add(User(id, user))
        return id
    }

    override fun getAllUsers(): List<User> {
        return db
    }

    override fun getUserById(id: Long): User? {
        var res: User? = null
        for (user:User in db){
            if (user.id == id){
                res = user
                break
            }
        }
        return res
    }

    override fun updateUser(id: Long, user: User): Int {
        val internalUser : User? = getUserById(id)
        return if(internalUser == null){
            -1
        }else{
            if (user.id != null){
                internalUser.id = user.id
            }
            if (user.oauth_id != null){
                internalUser.oauth_id = user.oauth_id
            }
            if (user.oauth_secret != null){
                internalUser.oauth_secret = user.oauth_secret
            }
            if (user.oauth_party != null){
                internalUser.oauth_party = user.oauth_party
            }
            if (user.email != null){
                internalUser.email = user.email
            }
            if (user.username != null){
                internalUser.username = user.username
            }
            if (user.avatar_url != null){
                internalUser.avatar_url = user.avatar_url
            }
            if (user.admin != null){
                internalUser.admin = user.admin
            }
            if (user.time != null){
                internalUser.time = user.time
            }
            1
        }
    }

    override fun deleteUser(id: Long): Int {
        val user : User? = getUserById(id)
        return if (user == null){
            -1
        }else{
            if (db.remove(user)){
                1
            }else{
                -1
            }
        }
    }

}