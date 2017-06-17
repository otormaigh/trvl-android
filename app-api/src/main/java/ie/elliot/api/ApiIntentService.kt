/*
 * Copyright (C) 2017 Elliot Tormey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ie.elliot.api

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import ie.elliot.api.model.Booking
import ie.elliot.api.model.Flight
import ie.elliot.api.model.RealmKey
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import timber.log.Timber

/**
 * @author Elliot Tormey
 * @since 05/06/2017
 */
class ApiIntentService : IntentService("ApiIntentService") {
    companion object {
        private val GET_AIRPORTS = "get_airports"
        private val GET_AIRLINES = "get_airlines"
        private val GET_FLIGHTS = "get_flights"
        private val POST_BOOKING = "post_booking"
        private val BOOKING_ID = "booking_id"

        fun getAllAirports(context: Context) {
            Intent(context, ApiIntentService::class.java).apply {
                putExtra(GET_AIRPORTS, true)
                context.startService(this)
            }
        }

        fun getAllAirlies(context: Context) {
            Intent(context, ApiIntentService::class.java).apply {
                putExtra(GET_AIRLINES, true)
                context.startService(this)
            }
        }

        fun getAllFlights(context: Context) {
            Intent(context, ApiIntentService::class.java).apply {
                putExtra(GET_FLIGHTS, true)
                context.startService(this)
            }
        }

        fun postBooking(context: Context, bookingId: Long) {
            Intent(context, ApiIntentService::class.java).apply {
                putExtra(POST_BOOKING, true)
                putExtra(BOOKING_ID, bookingId)
                context.startService(this)
            }
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        if (intent.getBooleanExtra(GET_AIRPORTS, false)) {
            getAirports()
        } else if (intent.getBooleanExtra(GET_AIRLINES, false)) {
            getAirlines()
        } else if (intent.getBooleanExtra(GET_FLIGHTS, false)) {
            getFlights()
        } else if (intent.getBooleanExtra(POST_BOOKING, false)) {
            postBooking(intent)
        }
    }

    private fun getAirports() {
        ApiClient.instance()
                .getAirports()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    airports ->
                    Timber.i("getAirports : onNext -> count = ${airports.size}")
                    Realm.getDefaultInstance().use { realm ->
                        realm.executeTransaction { realm ->
                            realm.insertOrUpdate(airports)
                        }
                    }
                }, {
                    error ->
                    Timber.e("getAirports : onError -> ${Log.getStackTraceString(error)}")
                })
    }

    private fun getAirlines() {
        ApiClient.instance()
                .getAirlines()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    airlines ->
                    Timber.i("getAirlines : onNext -> count = ${airlines.size}")
                    Realm.getDefaultInstance().use { realm ->
                        realm.executeTransaction { realm ->
                            realm.insertOrUpdate(airlines)
                        }
                    }
                }, {
                    error ->
                    Timber.e("getAirlines : onError -> ${Log.getStackTraceString(error)}")
                })
    }

    private fun getFlights() {
        ApiClient.instance()
                .getFlights()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    flights ->
                    Timber.i("getFlights : onNext -> count = ${flights.size}")
                    Realm.getDefaultInstance().use { realm ->
                        realm.executeTransaction { realm ->
                            realm.insertOrUpdate(flights)
                        }
                    }
                }, {
                    error ->
                    Timber.e("getFlights : onError -> ${Log.getStackTraceString(error)}")
                })
    }

    private fun postBooking(intent: Intent) {
        val bookingId = intent.getLongExtra(BOOKING_ID, 0)
        if (bookingId != 0L) {
            val realm = Realm.getDefaultInstance()
            val bookingRequest = realm.copyFromRealm(realm.where(Booking::class.java).equalTo(RealmKey.Booking.STARTED_AT, bookingId).findFirst())
            realm.close()

            ApiClient.instance()
                    .postBooking(bookingRequest!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({ bookingResponse ->
                        Timber.i("postBooking : onNext")
                        Realm.getDefaultInstance().use {
                            it.executeTransaction {
                                it.copyToRealmOrUpdate(bookingResponse)
                            }
                        }
                    }, {
                        error ->
                        Timber.e("postBooking : onError -> ${Log.getStackTraceString(error)}")
                    })
        }
    }
}