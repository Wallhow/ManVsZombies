package wallhow.vkdesktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Array
import com.vk.api.sdk.actions.Secure
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.ServiceClientCredentialsFlowResponse
import com.vk.api.sdk.objects.apps.Leaderboard
import com.vk.api.sdk.queries.apps.AppsGetLeaderboardType
import com.vk.api.sdk.queries.users.UserField
import wallhow.acentauri.vkgs.AppEvent
import wallhow.acentauri.vkgs.VKGameService

/**
 * Created by wallhow on 19.01.17.
 */
class VKGameServiceDesktop : VKGameService {
    private lateinit var pref : Preferences
    val vk: VkApiClient
    val app_id: Int = 5829473
    val client_secret = "BnnmDfghjyDr4ghjsS"
    private lateinit var userActor: UserActor
    private val serviceActor: ServiceActor
    private lateinit var secure: Secure
    private lateinit var myProfile: Profile

    init {
        val transportClient = HttpTransportClient.getInstance()
        vk = VkApiClient(transportClient)
        val authResponse = vk.oauth().serviceClientCredentialsFlow(app_id, client_secret).execute()
        serviceActor = ServiceActor(app_id, client_secret, authResponse.accessToken)
    }

    override fun auth(code: String) {
        pref = Gdx.app.getPreferences("service")
        if(pref.getToken()=="") {
            val auth = vk.oauth().userAuthorizationCodeFlow(app_id,client_secret,"",code).execute()
            pref.setToken(auth.accessToken)
            pref.setUserId(auth.userId)
        }


        userActor = UserActor(pref.getUserId(), pref.getToken())
        secure = Secure(vk)

        test()
    }
    private fun test() {
        val thisUser = vk.users().get(userActor).fields(UserField.MAIDEN_NAME).execute()[0]
        myProfile = Profile(thisUser.firstName,thisUser.lastName,getLevel())
    }

    override fun getRecordsUser() : Array<VKGameService.User> {
        val arr = Array<VKGameService.User>()

        getUsers().forEach {
            val info = vk.users().get(userActor).userIds("${it.id}").execute()[0]
            val user = VKGameService.User(it.id)
            user.firstName = info.firstName
            user.lastName = info.lastName
            arr.add(user)
        }

        return arr

    }

    override fun getMyProfile() : Profile {
        return myProfile
    }

    override fun getUsers(): Array<VKGameService.User> {
        val users = Array<VKGameService.User>()
        vk.friends().getAppUsers(userActor).execute().forEach {
            users.add(VKGameService.User(it))
        }
        return users
    }

    override fun getLevel(uid: Int): Int {
        val level = secure.getUserLevel(serviceActor,uid)
        return level.execute()[0].level
    }
    override fun getLevel(): Int {
        return getLevel(userActor.id)
    }

    override fun setLevel(level: Int) {
        println(vk.apps().getScore(userActor,app_id).execute())

        secure.setUserLevel(serviceActor).userId(userActor.id).level(level).execute()
        //addAppEvent(AppEvent.LEVEL,level)
    }

    override fun getRecords(global: Boolean): Array<Leaderboard> {
        val arr = Array<Leaderboard>()
        vk.apps().sendRequest(userActor,app_id)
        vk.apps().getLeaderboard(userActor,AppsGetLeaderboardType.LEVEL).global(global).execute().items.forEach {
            arr.add(it)
        }
        return arr
    }

    override fun addAppEvent(appEvent: AppEvent,value: Int) {
        vk.secure().addAppEvent(serviceActor,app_id,appEvent.activityId).value(value).execute()
    }
}

data class Profile (val firstName: String,val lastName:String,val level: Int)

private fun String.println() {
    println(this)
}
