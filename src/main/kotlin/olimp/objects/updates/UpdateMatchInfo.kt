package olimp.objects.updates

class UpdateMatchInfo {
    var id: Long = 0
    var lang = 0
    var name = ""
    var champId = 0
    var champName = ""
    var sportId = 0
    var start: Long = 0
    var maxBet = 0
    var comment = ""
    var state = UpdateState()
    var betRadarId = 0
    var brt1 = 0
    var brt2 = 0
    var specval = 0
    var sendTime: Long = 0
    var debug = UpdateDebug()
    var cleared = false
    var removed = false
    var maxbet = 0
}