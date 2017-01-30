package wallhow.acentauri.vkgs

import com.badlogic.gdx.utils.Array
import com.vk.api.sdk.objects.apps.Leaderboard
import wallhow.vkdesktop.Profile

/**
 * Created by wallhow on 19.01.17.
 */
interface VKGameService {

    fun auth ( code : String )
    fun getMyProfile() : Profile

    fun getRecords(global : Boolean) : Array<Leaderboard>
    fun getRecordsUser() : Array<VKGameService.User>
    fun getUsers() : Array<User>
    fun getLevel() : Int
    fun getLevel(uid: Int):Int

    fun setLevel(level: Int)
    fun addAppEvent(appEvent: AppEvent,value: Int)


    data class User(val id: Int) {
        var firstName = ""
        var lastName = ""
    }
}

enum class AppEvent(val activityId: Int) {
    LEVEL(1),SCORE(2)
}
