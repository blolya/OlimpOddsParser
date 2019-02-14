package olimp

import libs.websocket.WebsocketClient
import main.Odds
import main.OddsServiceInterface
import olimp.objects.ChannelRegistration
import olimp.objects.Ping
import olimp.objects.events.Event
import olimp.objects.updates.Update
import io.reactivex.subjects.PublishSubject
import olimp.objects.updates.UpdateOutcome

class OlimpOddsService(private val sportId: Int) : OddsServiceInterface {

    private val websocketClient: WebsocketClient?
    private val liveEvents: MutableMap<Long, Event> = mutableMapOf()
    private val oddsFlow: PublishSubject<Odds>? = PublishSubject.create()

    init {
        val self = this
        this.websocketClient = WebsocketClient(
            url = "wss://ruolimp.ru/eventbus/418/f0gdoqta/websocket",
            onOpen = { run {

                this.send( OlimpJSONParser.stringify( listOf( Ping() ) ) )
                this.send( OlimpJSONParser.stringify( listOf( ChannelRegistration(self.sportId) ) ) )

            } },
            onClose = { code, _, _ -> println(code) },
            onError = { exception -> println(exception) },
            onMessage = { frameData -> run {
                val incomingFrame = IncomingFrame(frameData)

                when( incomingFrame.getType() ) {
                    IncomingFrame.Types.PONG -> this.send( OlimpJSONParser.stringify( ( listOf( Ping() ) ) ) )
                    IncomingFrame.Types.UPDATE -> run {
                        val lastUpdates: List<Update?> = OlimpJSONParser.parseUpdate( incomingFrame.getFrameData() )

                        lastUpdates.forEach { lastUpdate -> run {
                            if (lastUpdate != null) {
                                val matchId = lastUpdate.body.matchInfo.id

                                if ( self.liveEvents.containsKey(matchId) ) {
                                    if (lastUpdate.body.matchInfo.removed) {
                                        self.liveEvents.remove(matchId)
                                    } else {
                                        lastUpdate.body.outcomes.forEach { outcome -> run {
                                            if (self.oddsFlow != null) self.oddsFlow.pourOdds(matchId, self.liveEvents, outcome)
                                        } }
                                    }
                                } else {
                                    self.liveEvents.put( matchId, Event(lastUpdate) )
                                    lastUpdate.body.outcomes.forEach { outcome -> run {
                                        if (self.oddsFlow != null) self.oddsFlow.pourOdds(matchId, self.liveEvents, outcome)
                                    } }
                                }
                            } else {
                                println("Empty update has arrived")
                            }
                        } }
                    }
                    IncomingFrame.Types.OPEN -> println("Websocket connection established")
                    else -> println("0")
                }
            } }
        )

        this.websocketClient.connect()
    }

    override fun getOddsFlow(): PublishSubject<Odds>? {
        return this.oddsFlow
    }
}

fun PublishSubject<Odds>.pourOdds(matchId: Long, liveEvents: MutableMap<Long, Event>, outcome: UpdateOutcome) {
    this.onNext(
        Odds(
            liveEvents.getValue(matchId).getSportId(),
            liveEvents.getValue(matchId).getSportName(),
            liveEvents.getValue(matchId).getMatchId(),
            liveEvents.getValue(matchId).getMatchName(),
            outcome.id,
            outcome.name,
            outcome.value,
            outcome.removed
        )
    )
}