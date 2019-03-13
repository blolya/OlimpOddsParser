package main

import com.beust.klaxon.Klaxon
import olimp.OlimpOddsService

fun main(args: Array<String>) {
    val osi: OddsServiceInterface = OlimpOddsService(1)
    val oddsFlow = osi.getOddsFlow()

    oddsFlow.subscribe { odds -> println( Klaxon().toJsonString(odds) ) }
}