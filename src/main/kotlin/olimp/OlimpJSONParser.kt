package olimp

import com.beust.klaxon.Klaxon
import olimp.objects.OlimpObject
import olimp.objects.updates.Update

object OlimpJSONParser {
    fun stringify(objectsList: List<Any>) : String {
        val jsonsList: MutableList<String> = mutableListOf()

        objectsList.forEach { obj -> jsonsList.add( Klaxon().toJsonString(obj) ) }

        return Klaxon().toJsonString(jsonsList)
    }

    fun parseUpdate(frameData: String?): List<Update?> {
        val jsonsObjectsList: List<String>?
        val objectsList: MutableList<Update?> = mutableListOf()

        if (frameData != null) {
            jsonsObjectsList = Klaxon().parseArray(frameData)
        } else {
            jsonsObjectsList = listOf()
        }

        jsonsObjectsList?.forEach { jsonsObject -> objectsList.add( Klaxon().parse(jsonsObject) ) }

        return objectsList.toList()
    }
}