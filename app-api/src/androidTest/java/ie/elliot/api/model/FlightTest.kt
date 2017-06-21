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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Elliot Tormey
 * @since 21/06/2017
 */
@RunWith(AndroidJUnit4::class)
internal class FlightTest {
    private companion object {
        @get:Rule
        val activityTestRule = ActivityTestRule<AndroidTestActivity>(AndroidTestActivity::class.java)
    }

    private val moshiAdapter = ApiClient.moshi.adapter(Flight::class.java)
    private val flight_json = """
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

    @Test
    fun testFromJson() {
        val flight: Flight? = moshiAdapter.fromJson(flight_json)
        assertTrue(flight != null)

        flight as Flight
        assertEquals("1", flight.id)
        assertEquals("1", flight.airline.id)
        assertEquals("swiss_air.png", flight.airline.logo)
        assertEquals("Swiss Air", flight.airline.name)
        assertEquals("SA123", flight.flight_code)
        assertEquals("123", flight.price)
        assertEquals("1969-07-16T13:32:00Z", flight.depart_at)
        assertEquals("1969-07-20T20:18:04Z", flight.arrive_at)
        assertEquals("13h 40m", flight.flight_time)
        assertEquals(0, flight.stop_count)
    }

    @Test
    fun testToJson() {

    }
}