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
import com.squareup.moshi.Types
import ie.elliot.api.Extensions.rawJsonToString
import ie.elliot.api.model.Airline
import ie.elliot.api.model.Airport
import ie.elliot.api.model.FlightResult
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

        fun getAllAirports(context: Context) {
            Intent(context, ApiIntentService::class.java).apply {
                putExtra(GET_AIRPORTS, true)
                context.startService(this)
            }
        }

        /**
         * Load any required test data into the Realm.
         */
        fun loadTestData(context: Context) {
            val airports = ApiClient.moshi
                    .adapter<List<Airport>>(Types.newParameterizedType(List::class.java, Airport::class.java))
                    .fromJson(context.rawJsonToString(R.raw.test_airports))
            val airlines = ApiClient.moshi
                    .adapter<List<Airline>>(Types.newParameterizedType(List::class.java, Airline::class.java))
                    .fromJson(context.rawJsonToString(R.raw.test_airlines))
            val flightResults = ApiClient.moshi
                    .adapter<List<FlightResult>>(Types.newParameterizedType(List::class.java, FlightResult::class.java))
                    .fromJson(context.rawJsonToString(R.raw.test_flight_results))


            Realm.getDefaultInstance().run {
                executeTransaction {
                    copyToRealmOrUpdate(airports)
                    copyToRealmOrUpdate(airlines)
                    copyToRealmOrUpdate(flightResults)
                }
            }
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        if (intent.getBooleanExtra(GET_AIRPORTS, false)) {
            ApiClient.instance()
                    .getAirports()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({
                        airports ->
                        Realm.getDefaultInstance().run {
                            executeTransaction {
                                insertOrUpdate(airports)
                            }
                        }
                    }, {
                        error ->
                        Timber.e("getAirports : onError -> ${Log.getStackTraceString(error)}")
                    })
        }
    }
}