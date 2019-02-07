package olimp.objects.events

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