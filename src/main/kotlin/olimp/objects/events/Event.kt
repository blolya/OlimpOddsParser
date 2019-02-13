package olimp.objects.events

import com.beust.klaxon.Klaxon
import io.reactivex.Observable
import main.Odds
import main.OddsServiceInterface
import olimp.objects.updates.Update
import olimp.objects.updates.UpdateOutcomes



class Event(update: Update) {

    private var id: Long = 0
    private var name: String = ""
    private var sportId: Int = 0
    private var outcomes = mutableMapOf<Long, UpdateOutcomes>()

    init {
        this.id = update.body.matchInfo.id
        this.name = update.body.matchInfo.name
        this.sportId = update.body.matchInfo.sportId
    }

    fun getId(): Long {
        return this.id
    }
    fun getName(): String {
        return this.name
    }
    fun getSportId(): Int {
        return this.sportId
    }

    fun pour(update: Update) {
        val oddsList = mutableListOf<Odds>()

        var self = this

        update.body.outcomes.forEach {
            outcome -> run {
                oddsList.add(Odds(self.sportId, self.id, self.name, outcome.id, outcome.name, outcome.value, outcome.removed))
            }
        }
        val oddsFlow: Observable<Odds> = Observable.fromIterable( oddsList )
        oddsFlow.subscribe({odds -> println(Klaxon().toJsonString(odds)) })
    }

    fun upgrade(update: Update) {
        var self = this

        update.body.outcomes.forEach { outcome -> run {
                if (self.outcomes.containsKey(outcome.id)) {
                    when(outcome.removed) {
                        true -> self.outcomes.remove(outcome.id)
                        else -> self.outcomes.replace(outcome.id, outcome)
                    }
                } else {
                    self.outcomes.put(outcome.id, outcome)
                }
            }
        }
    }
}