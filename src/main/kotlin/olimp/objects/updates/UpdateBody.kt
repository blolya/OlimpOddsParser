package olimp.objects.updates

import java.util.*

class UpdateBody {
    var version = 0
    var matchInfo = UpdateMatchInfo()
    var outcomes: List<UpdateOutcomes> = listOf()
}
