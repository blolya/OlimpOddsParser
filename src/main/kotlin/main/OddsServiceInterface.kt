package main

import io.reactivex.subjects.PublishSubject

interface OddsServiceInterface {
    fun getOddsFlow(): PublishSubject<Odds>?
}