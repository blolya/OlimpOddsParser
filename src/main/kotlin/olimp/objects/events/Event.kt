package olimp.objects.events

import olimp.objects.updates.Update

class Event(update: Update) {

    private var sportId: Int = update.body.matchInfo.sportId
    private var matchId: Long = update.body.matchInfo.id
    private var matchName: String = update.body.matchInfo.name

    fun getMatchId(): Long {
        return this.matchId
    }
    fun getMatchName(): String {
        return this.matchName
    }
    fun getSportId(): Int {
        return this.sportId
    }
}