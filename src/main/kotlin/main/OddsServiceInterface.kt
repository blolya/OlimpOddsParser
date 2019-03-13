package main

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface OddsServiceInterface {
    fun getOddsFlow(): Observable<Odds>
}