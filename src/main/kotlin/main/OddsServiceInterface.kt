package main

import olimp.objects.events.Event

interface OddsServiceInterface {
    fun getLiveEvents(): Map<Long, Event>
}