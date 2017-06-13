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
package ie.elliot.trvl.base

import ie.elliot.api.model.Airport
import ie.elliot.api.model.Booking
import ie.elliot.api.model.Flight
import ie.elliot.api.model.RealmKey
import io.realm.Realm
import timber.log.Timber

/**
 * @author Elliot Tormey
 * @since 13/06/2017
 */
internal class TrvlModel : AutoCloseable {
    private val realm: Realm = Realm.getDefaultInstance()
        get() {
            if (field.isClosed)
                field = Realm.getDefaultInstance()
            return field
        }

    override fun close() {
        Timber.i("closing model")
        realm.close()
    }

    fun getBookingById(bookingId: Long): Booking {
        return realm.where(Booking::class.java).equalTo(RealmKey.Common.ID, bookingId).findFirst()
    }

    fun createBooking(arriveAirport: String, departAirport: String): Long {
        val bookingId: Long = System.currentTimeMillis()
        realm.executeTransaction {
            // There should only be on 'Booking' object at a time in the Realm.
            // Delete all before creating a new one.
            realm.delete(Booking::class.java)

            val booking = Booking(id = bookingId)
            booking.flight = Flight()
            booking.flight.arrive_airport = getAirport(arriveAirport)
            booking.flight.depart_airport = getAirport(departAirport)
            realm.insertOrUpdate(booking)
        }
        return bookingId
    }

    fun getAirport(icao: String): Airport {
        return realm.where(Airport::class.java).equalTo(RealmKey.Airport.ICAO, icao).findFirst()
    }
}