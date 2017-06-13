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
package ie.elliot.api.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmModule
import java.util.*

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */


@RealmModule(library = true, allClasses = true)
class ApiRealmModule

object RealmKey {
    object Common {
        val ID = "id"
    }
    object Airport {
        val ICAO = "icao"
    }
}

open class Airport(
        @PrimaryKey
        var icao: String = "",
        var iata: String = "",
        var name: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        // TODO : Elliot -> Test data
        var price: String = "€321.30",
        // TODO : Elliot -> Test data
        var flightTime: String = "18h 13m") : RealmObject()

open class Flight(
        @PrimaryKey
        var id: String = "",
        var airline: Airline = Airline(),
        var price: String = "",
        // TODO: Elliot -> Moshi converter to read/write this as 'id' between API
        var depart_airport: Airport = Airport(),
        // TODO: Elliot -> Moshi converter to read/write this as 'id' between API
        var arrive_airport: Airport = Airport(),
        var depart_at: String = "",
        var arrive_at: String = "",
        var flight_time: String = "",
        var stop_count: Int = 0) : RealmObject()

open class Airline(
        @PrimaryKey
        var id: String = "",
        var logo: String = "",
        var name: String = "") : RealmObject()

open class Passenger(
        @PrimaryKey
        var id: String = "",
        var gender: Int = -1,
        var first_name: String = "",
        var last_name: String = "",
        var dob: String = "",
        var nationality: String = "",
        var passport_number: String = "",
        var passport_expiry: String = "") : RealmObject() {

    object Gender {
        val MALE = 0
        val FEMALE = 1
    }
}

open class Booking(
        @PrimaryKey
        var id: Long = 0,
        // TODO: Elliot -> Moshi converter to read/write this as 'id' between API
        var flight: Flight = Flight(),
        var in_progress: Boolean = true) : RealmObject()