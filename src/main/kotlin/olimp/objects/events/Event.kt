package olimp.objects.events

import olimp.objects.updates.Update

class Event(update: Update) {

    private val sportId: Int = update.body.matchInfo.sportId
    private val sportName: String = when(this.sportId) {
        1 -> "футбол"
        2 -> "хоккей"
        3 -> "теннис"
        4 -> "снукер"
        5 -> "баскетбол"
        6 -> "американский футбол"
        7 -> "биатлон"
        8 -> "хоккей с мячом"
        9 -> "гандбол"
        10 -> "волейбол"
        /*40 -> "настольный теннис"
        73 -> "крикет"
        112 -> "киберспорт"
        126 -> "пул"
        135 -> "шорт-хоккей"*/
        else -> ""
    }
    private val matchId: Long = update.body.matchInfo.id
    private val matchName: String = update.body.matchInfo.name

    fun getSportId(): Int {
        return this.sportId
    }
    fun getSportName(): String {
        return this.sportName
    }
    fun getMatchId(): Long {
        return this.matchId
    }
    fun getMatchName(): String {
        return this.matchName
    }
}