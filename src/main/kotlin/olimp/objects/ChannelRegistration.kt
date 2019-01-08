package olimp.objects

import java.util.HashMap

class ChannelRegistration : OlimpObject {

    constructor(channelId: Int) {
        this.address = "s" + channelId.toString() + "/0"
    }

    val type: String = "register"
    var address: String = "s1/0"
    val headers: HashMap<String, Any> = HashMap()
}