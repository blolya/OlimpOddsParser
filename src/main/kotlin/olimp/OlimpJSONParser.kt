package olimp

import com.beust.klaxon.Klaxon
import olimp.objects.OlimpObject
import olimp.objects.updates.Update
import java.io.IOException
import java.util.*

object OlimpJSONParser {
    fun stringify(objectsList: List<OlimpObject>) : String {
        val jsonsList: MutableList<String> = mutableListOf()

        objectsList.forEach { obj -> jsonsList.add( Klaxon().toJsonString( obj ) ) }

        return Klaxon().toJsonString( jsonsList )
    }

    fun parseUpdate(frameData: String?): List<Update?> {
        val objectsList: MutableList<Update?> = mutableListOf()
        val jsonsObjectsList: List<String>? = Klaxon().parseArray( frameData!! )

        jsonsObjectsList?.forEach { jsonsObject -> objectsList.add( Klaxon().parse( jsonsObject ) ) }

        return objectsList.toList()
    }
}