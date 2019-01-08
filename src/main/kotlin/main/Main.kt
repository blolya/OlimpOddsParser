package main

import olimp.OlimpOddsService

fun main(args: Array<String>) {
    val osi: OddsServiceInterface = OlimpOddsService(1)

    osi.getLiveOdds()
}