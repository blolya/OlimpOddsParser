package main

import io.reactivex.subjects.PublishSubject
import olimp.objects.events.Event

interface OddsServiceInterface {
    fun getOddsFlow(): PublishSubject<Odds>
}