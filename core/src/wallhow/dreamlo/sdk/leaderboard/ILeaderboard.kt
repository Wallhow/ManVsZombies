package wallhow.dreamlo.sdk.leaderboard

import wallhow.dreamlo.sdk.utils.User
import java.util.*

/**
 * Created by wallhow on 24.02.17.
 */
interface ILeaderboard {
    fun setScore(name: String, score: Int)
    fun setUser(user: User)
    fun getAllUsers() : Array<User>?
    fun getTopUsers(top: Int) : Array<User>
    fun getUsers(firstIndex : Int, countUser : Int) : Array<User>
    fun getUser(name: String) : User
    fun getScore(name: String) : Int
    fun fromJson(json : String) : HashMap<String, User>
    fun getJson() : String

    fun saveInLocalStorage()
}