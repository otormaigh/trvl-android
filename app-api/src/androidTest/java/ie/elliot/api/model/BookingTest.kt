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

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import ie.elliot.api.AndroidTestActivity
import ie.elliot.api.ApiClient
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

/**
 * @author Elliot Tormey
 * @since 21/06/2017
 */
@RunWith(AndroidJUnit4::class)
internal class BookingTest {
    private companion object {
        @get:Rule
        val activityTestRule = ActivityTestRule<AndroidTestActivity>(AndroidTestActivity::class.java)
    }

    private val test_booking_json = """
    {
      "id": "1",
      "started_at": "2017-06-15T09:43:43+0100",
      "flight": "SA123",
      "in_progress": true
    }
    """

    @Before
    fun setUp() {
        activityTestRule.launchActivity(null)

        Realm.init(activityTestRule.activity)
        val realmConfig = RealmConfiguration.Builder()
                .inMemory()
                .name("trvl-test.realm")
                .schemaVersion(0)
                .modules(Realm.getDefaultModule(), ApiRealmModule())
                .build()

        Realm.setDefaultConfiguration(realmConfig)
        Assert.assertEquals(Realm.getDefaultInstance().configuration, realmConfig)
    }

    @After
    fun tearDown() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realm ->
                realm.deleteAll()
            }
        }
    }

    @Test
    fun testFromJson() {
        val flight_json = """
            {
              "id": "1",
              "airline": {
                "id": "1",
                "logo": "swiss_air.png",
                "name": "Swiss Air"
              },
              "flight_code": "SA123",
              "price": "123",
              "depart_at": "1969-07-16T13:32:00Z",
              "arrive_at": "1969-07-20T20:18:04Z",
              "flight_time": "13h 40m",
              "stop_count": 0
            }
            """

        Realm.getDefaultInstance().use {
            it.executeTransaction {
                it.insertOrUpdate(ApiClient.moshi.adapter(Flight::class.java).fromJson(flight_json))
            }
        }

        val booking: Booking? = ApiClient.moshi.adapter(Booking::class.java).fromJson(test_booking_json)
        assertTrue(booking != null)

        booking as Booking
        assertEquals("1", booking.id)
        assertEquals(1497516223000, booking.started_at)
        assertEquals("SA123", booking.flight?.flight_code)
        assertEquals("Swiss Air", booking.flight?.airline?.name)
        assertEquals(true, booking.in_progress)
    }

    @Test
    fun testFromJsonBad() {
        val booking: Booking? = ApiClient.moshi.adapter(Booking::class.java).fromJson(test_booking_json)
        assertTrue(booking != null)

        booking as Booking
        assertEquals("1", booking.id)
        assertEquals(1497516223000, booking.started_at)
        assertEquals("SA123", booking.flight?.flight_code)
        assertEquals("", booking.flight?.airline?.name)
        assertEquals(true, booking.in_progress)
    }

    @Test
    fun testToJson() {
        val flight: Flight = Flight()
        val booking = Booking(
                flight = flight
        )

        val booking_json: String? = ApiClient.moshi.adapter(Booking::class.java).toJson(booking)
        assertTrue(booking_json != null)
    }
}