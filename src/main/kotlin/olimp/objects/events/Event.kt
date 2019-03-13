package olimp.objects.events

import olimp.objects.updates.Update

class Event(update: Update) {

    val sportId: Int = update.body.matchInfo.sportId
    val sportName: String = when(this.sportId) {
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
    val matchId: Long = update.body.matchInfo.id
    val matchName: String = update.body.matchInfo.name


}