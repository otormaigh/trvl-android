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
package ie.elliot.trvl.ui.view

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.View
import ie.elliot.api.model.Airport
import ie.elliot.trvl.ui.test.R
import kotlinx.android.synthetic.main.airport_item_view.view.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Elliot Tormey
 * @since 10/06/2017
 */
@RunWith(AndroidJUnit4::class)
class TestAirportView {
    private val testContext by lazy { InstrumentationRegistry.getContext() }

    private lateinit var airportView: AirportView
    private lateinit var airport: Airport

    @Before
    fun setUp() {
        airportView = AirportView(testContext)
        airport = Airport(name = "New York", iata = "JFK")
    }

    @Test
    fun testDefaultValues() {
        assertTrue(airportView.airport == null)
        assertTrue(airportView.hint == 0)
        assertTrue(airportView.llAirport.visibility == View.GONE)
        assertTrue(airportView.llHint.visibility == View.GONE)
    }

    @Test
    fun testShowHint() {
        airportView.hint = R.string.test_hint
        assertTrue(airportView.llAirport.visibility == View.GONE)
        assertTrue(airportView.llHint.visibility == View.VISIBLE)

        assertTrue(airportView.tvHint.text == "Where to?")
    }

    @Test
    fun testShowAirport() {
        airportView.airport = airport
        assertTrue(airportView.llHint.visibility == View.GONE)
        assertTrue(airportView.llAirport.visibility == View.VISIBLE)

        assertTrue(airportView.tvLocation.text == "New York")
        assertTrue(airportView.tvAirport.text == "JFK")
    }

    @Test
    fun showHintThenAirport() {
        airportView.hint = R.string.test_hint
        assertTrue(airportView.llAirport.visibility == View.GONE)
        assertTrue(airportView.llHint.visibility == View.VISIBLE)

        assertTrue(airportView.tvHint.text == "Where to?")

        airportView.airport = airport
        assertTrue(airportView.llHint.visibility == View.GONE)
        assertTrue(airportView.llAirport.visibility == View.VISIBLE)

        assertTrue(airportView.tvLocation.text == "New York")
        assertTrue(airportView.tvAirport.text == "JFK")
    }
}