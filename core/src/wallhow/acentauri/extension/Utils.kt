package wallhow.acentauri.extension

/**
 * Created by wallhow on 03.04.17.
 */

fun Any.logLn(obj: Any) {
    val name = this.javaClass.canonicalName.substring(this.javaClass.canonicalName.lastIndexOf('.')+1)
    println(name + " : $obj")
}

infix fun Boolean.isTrue(process : () -> Unit) {
    if(this)
        process.invoke()
}