package wallhow.dreamlo.sdk.admin

import wallhow.dreamlo.sdk.DreamloSDK
import wallhow.dreamlo.sdk.utils.User

/**
 * Created by wallhow on 24.02.17.
 */
class AdminService {

    fun deletUser(name: String) {
        DreamloSDK.privateExecute("delete/$name")
    }
    fun deletUser(user: User) {
        DreamloSDK.privateExecute("delete/$user.name")
    }
    fun clearAll() {
        DreamloSDK.privateExecute("clear")
    }
}