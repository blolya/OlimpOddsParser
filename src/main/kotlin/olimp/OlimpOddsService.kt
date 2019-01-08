package olimp

import com.beust.klaxon.Klaxon
import libs.websocket.WebsocketClient
import main.OddsServiceInterface
import olimp.objects.ChannelRegistration
import olimp.objects.Ping
import olimp.objects.updates.Update


class OlimpOddsService : OddsServiceInterface {

    constructor(channelId: Int) {
        val self = this

        this.channelId = channelId

        this.websocketClient = WebsocketClient(
            url = "wss://ruolimp.ru/eventbus/199/f3onps3k/websocket",
            onOpen = { run {

                this.send( OlimpJSONParser.stringify( listOf( Ping() ) ) )
                this.send( OlimpJSONParser.stringify( listOf( ChannelRegistration( self.channelId ) ) ) )

            } },
            onClose = { code, _, _ -> println(code) },
            onError = { exception -> println(exception) },
            onMessage = { frameData -> run {

                val incomingFrame = IncomingFrame(frameData)

                when( incomingFrame.getType() ) {

                    IncomingFrame.Types.PONG -> this.send( OlimpJSONParser.stringify( ( listOf( Ping() ) ) ) )
                    IncomingFrame.Types.UPDATE -> run {

                        val lastUpdates : List<Update?> = OlimpJSONParser.parseUpdate( incomingFrame.getFrameData() )

                        lastUpdates.forEach { lastUpdate -> println( lastUpdate?.body?.outcomes?.toString() ) }
                    }
                    else -> println("0")

                }
            } }
        )

        this.websocketClient.connect()
    }

    override fun getLiveOdds() {
        println( this.channelId )
    }

    private var channelId: Int = 0
    private val websocketClient: WebsocketClient?
}