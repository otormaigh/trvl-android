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

package ie.elliot.api.moshi_adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import ie.elliot.api.model.Flight
import ie.elliot.api.model.RealmKey
import io.realm.Realm
import timber.log.Timber

/**
 * @author Elliot Tormey
 * @since 15/06/2017
 */

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class ToId

internal class FlightToIdAdapter {
    @FromJson @ToId
    fun fromJson(flight_code: String): Flight? {
        Timber.i("FlightToIdAdapter -> fromJson")
        val realm = Realm.getDefaultInstance()
        val flight = realm.copyFromRealm(realm.where(Flight::class.java).equalTo(RealmKey.Flight.FLIGHT_CODE, flight_code).findFirst())
        realm.close()
        return flight
    }

    @ToJson
    fun toJson(@ToId flight: Flight): String {
        Timber.i("FlightToIdAdapter -> toJson")
        return flight.flight_code
    }
}