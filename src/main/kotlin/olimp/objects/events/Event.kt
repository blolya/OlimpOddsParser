package olimp.objects.events

import olimp.objects.updates.Update
import olimp.objects.updates.UpdateOutcomes

class Event(private val id: Long) {
    fun update(update: Update) {
        var self = this
        this.lastUpdate = update

        this.lastUpdate.body.outcomes.forEach { outcome -> run {
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

    fun getId(): Long {
        return this.id
    }

    private var lastUpdate = Update()
    private var outcomes = mutableMapOf<Long, UpdateOutcomes>()
}