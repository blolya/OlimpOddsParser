package olimp

import io.reactivex.Observable
import libs.websocket.WebsocketClient
import main.Odds
import main.OddsServiceInterface
import olimp.objects.ChannelRegistration
import olimp.objects.Ping
import olimp.objects.events.Event
import olimp.objects.updates.Update
import io.reactivex.subjects.PublishSubject
import olimp.objects.updates.UpdateOutcome
import olimp.OlimpMessageType.*


class OlimpOddsService(private val sportId: Int) : OddsServiceInterface {

    private val websocket: WebsocketClient by lazy {
        WebsocketClient(
            url = "wss://ruolimp.ru/eventbus/418/f0gdoqta/websocket",
            onOpen = {
                websocket.send( OlimpJSONParser.stringify( listOf( Ping() ) ) )
                websocket.send( OlimpJSONParser.stringify( listOf( ChannelRegistration("s${sportId}/0" ) ) ) )
            },
            onClose = { code, _, _ -> println(code) },
            onError = { exception -> println(exception) },
            onMessage = { frameData ->
                val messageType: OlimpMessageType? = enumValue(frameData?.first())

                when (messageType) {
                    PING -> websocket.send( OlimpJSONParser.stringify( ( listOf( Ping() ) ) ) )
                    UPDATE -> {
                        val updates = OlimpJSONParser.parseUpdate(frameData?.substring(1))
                        updates.forEach { apply(it) }
                    }
                    OPEN -> println("Websocket connection established")
                    else -> println("0")
                }
            }
        )
    }

    fun apply(update: Update?) {
        update?.let {
            val matchId = it.body.matchInfo.id


            if ( liveEvents.containsKey(matchId) ) {
                if (update.body.matchInfo.removed) {
                    liveEvents.remove(matchId)
                } else {
                    apply(update.body.outcomes, matchId)
                }
            } else {
                addNewLiveEvent(matchId, update)
            }

        }

    }


    fun apply(outcomes: List<UpdateOutcome>, matchId: Long) {
        outcomes.forEach { outcome ->
            oddsFlow.pourOdds(matchId, liveEvents, outcome)
        }
    }

    fun addNewLiveEvent(matchId: Long, update: Update) {
        websocket.send( OlimpJSONParser.stringify( listOf( ChannelRegistration("$matchId/0" ) ) ) )
        liveEvents[matchId] = Event(update)
        apply(update.body.outcomes, matchId)
    }

//    fun removeEventIfNeed(matchId: Long, update: Update): Boolean {
//
//    }

    private val liveEvents: MutableMap<Long, Event> = mutableMapOf()
    private val oddsFlow: PublishSubject<Odds> = PublishSubject.create()

    init {
        this.websocket.connect()
    }

    override fun getOddsFlow(): Observable<Odds> {
        return this.oddsFlow
    }
}

fun PublishSubject<Odds>.pourOdds(matchId: Long, liveEvents: MutableMap<Long, Event>, outcome: UpdateOutcome) {
    this.onNext(
        Odds(
            liveEvents.getValue(matchId).sportId,
            liveEvents.getValue(matchId).sportName,
            liveEvents.getValue(matchId).matchId,
            liveEvents.getValue(matchId).matchName,
            outcome.id,
            outcome.name,
            outcome.value,
            outcome.removed
        )
    )
}