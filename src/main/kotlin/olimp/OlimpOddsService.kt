package olimp

import com.beust.klaxon.Klaxon
import libs.websocket.WebsocketClient
import main.OddsServiceInterface
import olimp.objects.ChannelRegistration
import olimp.objects.Ping
import olimp.objects.events.Event
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

                        lastUpdates.forEach {
                                lastUpdate -> run {
                                    if (lastUpdate != null) {
                                        val matchId = lastUpdate.body.matchInfo.id
                                        if (self.liveEvents.containsKey( matchId )) {
                                            println("The ${matchId} event is already in list")
                                            when (lastUpdate.body.matchInfo.removed) {
                                                true -> run {
                                                    self.liveEvents.remove(matchId)
                                                    println("The ${matchId} event has been removed")
                                                }
                                                else -> self.liveEvents.getValue(matchId).update(lastUpdate)
                                            }
                                        } else {
                                            self.liveEvents.put(matchId, Event(matchId))
                                            println("The ${matchId} event has been added to the list")
                                        }
                                    }
                            }
                        }
                    }
                    else -> println("0")

                }
            } }
        )

        this.websocketClient.connect()
    }

    override fun getLiveEvents(): Map<Long, Event> {
        return this.liveEvents
    }

    private var channelId: Int = 0
    private val websocketClient: WebsocketClient?
    private var liveEvents: MutableMap<Long, Event> = mutableMapOf()
}

fun MutableMap<Long, Event>.toString(): String {
    return Klaxon().toJsonString(this)
}