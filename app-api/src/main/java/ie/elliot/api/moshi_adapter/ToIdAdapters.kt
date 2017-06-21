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

/**
 * @author Elliot Tormey
 * @since 15/06/2017
 */
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class ToId

internal class FlightToIdAdapter {
    @FromJson @ToId
    fun fromJson(json: Any): Flight? {
        if (json is Flight) return json
        if (json is String) {
            val realm = Realm.getDefaultInstance()
            val flight = realm.where(Flight::class.java).equalTo(RealmKey.Flight.FLIGHT_CODE, json).findFirst()
            if (flight != null) return realm.copyFromRealm(flight)
            realm.close()
            return Flight(flight_code = json)
        }
        return null
    }

    @ToJson
    fun toJson(@ToId flight: Flight): String {
        return flight.flight_code
    }
}