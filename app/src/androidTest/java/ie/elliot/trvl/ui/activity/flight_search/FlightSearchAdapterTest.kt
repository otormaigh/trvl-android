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
package ie.elliot.trvl.ui.activity.flight_search

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ie.elliot.api.model.Airline
import ie.elliot.api.model.Airport
import ie.elliot.api.model.ApiRealmModule
import ie.elliot.api.model.Flight
import ie.elliot.trvl.R
import ie.elliot.trvl.test_helper.Android
import ie.elliot.trvl.test_helper.RecyclerViewMatcher
import ie.elliot.trvl.ui.activity.passenger_detail.PassengerDetailActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmRecyclerViewAdapter
import kotlinx.coroutines.experimental.launch
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Android test for [FlightSearchAdapter] class. Because the adapter is using the [RealmRecyclerViewAdapter],
 * any time we interact with it we must wrap it in a looper thread to make sure the async query
 * attached to the adapter executes successfully. Use [kotlinx.coroutines.experimental.launch] for this e.g.
 * ```
 * launch(Android) {
 *   // Execute test here
 * }
 * ```
 *
 * @author Elliot Tormey
 * @since 11/06/2017
 */
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
@RunWith(AndroidJUnit4::class)
internal class FlightSearchAdapterTest {
    private companion object {
        @get:Rule
        val activityTestRule = ActivityTestRule<FlightSearchActivity>(FlightSearchActivity::class.java)

        fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
            return RecyclerViewMatcher(recyclerViewId)
        }
    }

    private var rvFlightResult: RecyclerView? = null
    private var flightSearchAdapter: FlightSearchAdapter? = null

    @Before
    fun setUp() {
        activityTestRule.launchActivity(null)

        launch(Android) {
            val realmConfig = RealmConfiguration.Builder()
                    .inMemory()
                    .name("trvl-test.realm")
                    .schemaVersion(0)
                    .modules(Realm.getDefaultModule(), ApiRealmModule())
                    .build()

            Realm.setDefaultConfiguration(realmConfig)
            assertEquals(Realm.getDefaultInstance().configuration, realmConfig)

            rvFlightResult = activityTestRule.activity.findViewById(R.id.rvFlightsResults) as RecyclerView
            flightSearchAdapter = FlightSearchAdapter(activityTestRule.activity as FlightSearchView)
            assertNotNull(rvFlightResult)
            assertNotNull(flightSearchAdapter)

            rvFlightResult?.adapter = flightSearchAdapter
            rvFlightResult?.layoutManager = LinearLayoutManager(activityTestRule.activity)
            rvFlightResult!!.visibility = View.VISIBLE

            assertTrue(rvFlightResult!!.isAttachedToWindow)
            assertTrue(rvFlightResult!!.visibility == View.VISIBLE)
        }
    }

    @After
    fun tearDown() {
        rvFlightResult = null
        flightSearchAdapter = null

        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realm ->
                realm.deleteAll()
            }
        }
    }

    @Test
    fun testDefaults() {
        launch(Android) {
            assertEquals(0, flightSearchAdapter?.itemCount)
        }
    }

    @Test
    fun testWithData() {
        launch(Android) {
            assertEquals(0, flightSearchAdapter?.itemCount)
            realmInsertTestData()

            // Sleep for a bit so the async query can update.
            Thread.sleep(500)
            assertEquals(2, flightSearchAdapter?.itemCount)
        }
        onView(withRecyclerView(R.id.rvFlightsResults).atPositionOnView(0, R.id.tvStops)).check(matches(withText(R.string.non_stop)))

        onView(withRecyclerView(R.id.rvFlightsResults).atPositionOnView(1, R.id.tvAirlineName)).check(matches(withText("Air New Zealand")))
        onView(withRecyclerView(R.id.rvFlightsResults).atPositionOnView(1, R.id.tvDepartAt)).check(matches(withText("1969-07-16 13:32:00Z")))
        onView(withRecyclerView(R.id.rvFlightsResults).atPositionOnView(1, R.id.tvArriveAt)).check(matches(withText("1969-07-20 20:18:04Z")))
        onView(withRecyclerView(R.id.rvFlightsResults).atPositionOnView(1, R.id.tvFlightTime)).check(matches(withText("13h 40m")))
        onView(withRecyclerView(R.id.rvFlightsResults).atPositionOnView(1, R.id.tvPrice)).check(matches(withText("321")))
        onView(withRecyclerView(R.id.rvFlightsResults).atPositionOnView(1, R.id.tvStops)).check(matches(withText("1 Stop")))

        Thread.sleep(500)
    }

    @Test
    fun testItemClick() {
        Intents.init()
        launch(Android) {
            realmInsertTestData()
        }
        Thread.sleep(500)

        onView(withRecyclerView(R.id.rvFlightsResults).atPosition(0)).perform(click())
        intended(hasComponent(PassengerDetailActivity::class.java.name))
        Intents.release()
    }

    private fun realmInsertTestData() {
        Realm.getDefaultInstance().use { realm ->
            assertEquals(0, realm.where(Flight::class.java).findAll().size)

            val airlineOne = Airline("1", "swiss_air.png", "Swiss Air")
            val airlineTwo = Airline("2", "air_nz.png", "Air New Zealand")
            val airportOne = Airport("KJFK", "JFK", "New York", 40.639722, -73.778889)
            val airportTwo = Airport("EIDW", "DUB", "Dublin", 53.421389, -6.27)
            val flightOne = Flight("1", airlineOne, "123", airportOne, airportTwo, "1969-07-16 13:32:00Z", "1969-07-20 20:18:04Z", "13h 40m", 0)
            val flightTwo = Flight("2", airlineTwo, "321", airportTwo, airportOne, "1969-07-16 13:32:00Z", "1969-07-20 20:18:04Z", "13h 40m", 1)

            realm.executeTransaction { realm ->
                realm.insertOrUpdate(flightOne)
                realm.insertOrUpdate(flightTwo)
            }
            assertEquals(2, realm.where(Flight::class.java).findAll().size)
        }
    }
}