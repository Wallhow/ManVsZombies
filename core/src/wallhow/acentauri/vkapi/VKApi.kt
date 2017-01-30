package wallhow.acentauri.vkapi

/**
 * Created by wallhow on 14.01.17.
 */
interface VKApi {
    fun authorization(access_token : String)

    fun getUser() : VKUser?

}

interface VKUser {
    fun getLevel(uid: Int) : Int
    fun getMyLevel() : Int
    fun setLevel(level: Int)
    fun addAppEvent(event: VKEvent)
}

enum class VKEvent {

}
