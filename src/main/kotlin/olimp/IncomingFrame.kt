package olimp

class IncomingFrame {
    constructor(frameData: String?) {
        when(frameData?.first()) {
            'o' -> this.type = Types.OPEN
            'a' -> { this.type = Types.UPDATE; this.frameData = frameData.substring(1)}
            'h' -> this.type = Types.PONG
            else -> this.type = Types.ERROR
        }
    }

    fun getType() : Types {
        return this.type
    }
    fun getFrameData() : String? {
        return this.frameData
    }

    enum class Types {
        UPDATE,
        PONG,
        OPEN,
        ERROR
    }

    private var type: Types = Types.UPDATE
    private var frameData: String? = ""
}