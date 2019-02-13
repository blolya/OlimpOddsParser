package olimp

import libs.websocket.WebsocketClient
import main.Odds
import main.OddsServiceInterface
import olimp.objects.ChannelRegistration
import olimp.objects.Ping
import olimp.objects.events.Event
import olimp.objects.updates.Update
import io.reactivex.subjects.PublishSubject




class OlimpOddsService(private var channelId: Int) : OddsServiceInterface {

    private val websocketClient: WebsocketClient?
    private var liveEvents: MutableMap<Long, Event> = mutableMapOf()

    private val oddsFlow: PublishSubject<Odds> = PublishSubject.create()

    init {
        val self = this
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
                                            //println("The ${matchId} event is already in list")
                                            when (lastUpdate.body.matchInfo.removed) {
                                                true -> run {
                                                    self.liveEvents.remove(matchId)
                                                    //println("The ${matchId} event has been removed")
                                                }
                                                else -> run {
                                                    lastUpdate.body.outcomes.forEach { outcome ->
                                                        self.oddsFlow.onNext(
                                                            Odds(
                                                                self.liveEvents.getValue(matchId).getSportId(),
                                                                self.liveEvents.getValue(matchId).getId(),
                                                                self.liveEvents.getValue(matchId).getName(),
                                                                outcome.id,
                                                                outcome.name,
                                                                outcome.value,
                                                                outcome.removed
                                                            ))
                                                    }
                                                }
                                            }
                                        } else {
                                            self.liveEvents.put(matchId, Event(lastUpdate))
                                            //println("The ${matchId} event has been added to the list")
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

    override fun getOddsFlow(): PublishSubject<Odds> {
        return this.oddsFlow
    }
}