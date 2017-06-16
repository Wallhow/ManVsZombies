package wallhow.manvszombies.game.objects

import wallhow.acentauri.extension.logLn

/**
 * Created by wallhow on 31.05.17.
 */
object Options {
    var saveRecords = false
    var log = true

}
fun Any.log(any : Any) {
    if(Options.log)
        this.logLn(any)
}