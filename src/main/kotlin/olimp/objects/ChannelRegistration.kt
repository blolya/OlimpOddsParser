package olimp.objects

import java.util.HashMap

class ChannelRegistration(val address: String): OlimpObject() {
    val type: String = "register"
    val headers: HashMap<String, Any> = HashMap()
}