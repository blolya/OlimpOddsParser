package olimp.objects.updates

class UpdateBody {
    var version = 0
    var matchInfo = UpdateMatchInfo()
    var outcomes: List<UpdateOutcome> = listOf()
}
