package olimp.objects.updates

import olimp.objects.OlimpObject

class Update: OlimpObject() {
    var type = ""
    var address = ""
    var headers = UpdateHeaders()
    var body = UpdateBody()
}