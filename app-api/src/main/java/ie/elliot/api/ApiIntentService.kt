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