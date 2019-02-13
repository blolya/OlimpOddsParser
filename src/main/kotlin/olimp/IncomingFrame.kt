package olimp

class IncomingFrame(private var frameData: String?) {
    private var type: Types = Types.UPDATE

    init {
        when( frameData?.first() ) {
            'o' -> this.type = Types.OPEN
            'a' -> { this.type = Types.UPDATE; this.frameData = frameData?.substring(1)}
            'h' -> this.type = Types.PONG
            else -> this.type = Types.ERROR
        }
    }

    fun getType(): Types {
        return this.type
    }
    fun getFrameData(): String? {
        return this.frameData
    }

    enum class Types {
        UPDATE,
        PONG,
        OPEN,
        ERROR
    }
}