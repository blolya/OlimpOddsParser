package olimp.objects

import java.util.HashMap

class ChannelRegistration(channelId: Int): OlimpObject() {
    val type: String = "register"
    var address: String = "s1/0"
    val headers: HashMap<String, Any> = HashMap()

    init {
        this.address = "s" + channelId.toString() + "/0"
    }
}