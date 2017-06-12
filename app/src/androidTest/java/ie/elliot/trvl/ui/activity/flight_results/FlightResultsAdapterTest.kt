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
package ie.elliot.trvl.ui.activity.flight_results

import android.os.Handler
import android.os.Looper
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
import ie.elliot.api.model.Airline
import ie.elliot.api.model.ApiRealmModule
import ie.elliot.api.model.Flight
import ie.elliot.trvl.R
import ie.elliot.trvl.test_helper.Android
import ie.elliot.trvl.test_helper.RecyclerViewMatcher
import ie.elliot.trvl.ui.activity.passenger_detail.PassengerDetailActivity
import io.realm.Realm
import org.junit.runner.RunWith
import io.realm.RealmConfiguration
import io.realm.RealmRecyclerViewAdapter
import kotlinx.coroutines.experimental.launch
import org.junit.*
import org.junit.Assert.*

/**
 * Android test for [FlightResultsAdapter] class. Because the adapter is using the [RealmRecyclerViewAdapter],
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
internal class FlightResultsAdapterTest {
    private companion object {
        @get:Rule
        val flightResultActivityTestRule = ActivityTestRule<FlightResultsActivity>(FlightResultsActivity::class.java)

        fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
            return RecyclerViewMatcher(recyclerViewId)
        }

        fun withRecyclerView(recyclerView: RecyclerView): RecyclerViewMatcher {
            return withRecyclerView(recyclerView.id)
        }
    }

    private val mockRecyclerView: RecyclerView by lazy { flightResultActivityTestRule.activity.findViewById(R.id.rvFlightsResults) as RecyclerView }
    private val flightResultAdapter by lazy { FlightResultsAdapter(flightResultActivityTestRule.activity) }
    private val mockRealmConfig by lazy {
        RealmConfiguration.Builder()
                .inMemory()
                .name("trvl-test.realm")
                .schemaVersion(0)
                .modules(Realm.getDefaultModule(), ApiRealmModule())
                .build()
    }

    @Before
    fun setUp() {
        flightResultActivityTestRule.launchActivity(null)
        launch(Android) {
            Realm.setDefaultConfiguration(mockRealmConfig)
            assertEquals(Realm.getDefaultInstance().configuration, mockRealmConfig)

            mockRecyclerView.adapter = flightResultAdapter
            mockRecyclerView.layoutManager = LinearLayoutManager(flightResultActivityTestRule.activity)
            assertTrue(mockRecyclerView.isAttachedToWindow)
        }
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
    fun testDefaults() {
        launch(Android) {
            assertEquals(0, flightResultAdapter.itemCount)
        }
    }

    @Test
    fun testWithData() {
        launch(Android) {
            Handler(Looper.getMainLooper()).post {
                assertEquals(0, flightResultAdapter.itemCount)
                realmInsertTestData()

                // Sleep for a bit so the async query can update.
                Thread.sleep(100)
                assertEquals(2, flightResultAdapter.itemCount)

            }
        }
        onView(withRecyclerView(mockRecyclerView).atPositionOnView(0, R.id.tvStops)).check(matches(withText(R.string.non_stop)))

        onView(withRecyclerView(mockRecyclerView).atPositionOnView(1, R.id.tvAirlineName)).check(matches(withText("Air New Zealand")))
        onView(withRecyclerView(mockRecyclerView).atPositionOnView(1, R.id.tvDepartAt)).check(matches(withText("1969-07-16 13:32:00Z")))
        onView(withRecyclerView(mockRecyclerView).atPositionOnView(1, R.id.tvArriveAt)).check(matches(withText("1969-07-20 20:18:04Z")))
        onView(withRecyclerView(mockRecyclerView).atPositionOnView(1, R.id.tvFlightTime)).check(matches(withText("13h 40m")))
        onView(withRecyclerView(mockRecyclerView).atPositionOnView(1, R.id.tvPrice)).check(matches(withText("321")))
        onView(withRecyclerView(mockRecyclerView).atPositionOnView(1, R.id.tvStops)).check(matches(withText("1 Stop")))

        Thread.sleep(500)
    }

    @Test
    fun testItemClick() {
        Intents.init()
        launch(Android) {
            Handler(Looper.getMainLooper()).post {
                realmInsertTestData()
            }
        }
        Thread.sleep(100)

        onView(withRecyclerView(mockRecyclerView).atPosition(0)).perform(click())
        intended(hasComponent(PassengerDetailActivity::class.java.name))
        Intents.release()
    }

    private fun realmInsertTestData() {
        Realm.getDefaultInstance().use { realm ->
            val airlineOne = Airline("1", "swiss_air.png", "Swiss Air")
            val airlineTwo = Airline("2", "air_nz.png", "Air New Zealand")
            val flightOne = Flight("1", airlineOne, "123", "1969-07-16 13:32:00Z", "1969-07-20 20:18:04Z", "13h 40m", 0)
            val flightTwo = Flight("2", airlineTwo, "321", "1969-07-16 13:32:00Z", "1969-07-20 20:18:04Z", "13h 40m", 1)

            realm.executeTransaction { realm ->
                realm.insertOrUpdate(flightOne)
                realm.insertOrUpdate(flightTwo)
            }
            assertEquals(2, realm.where(Flight::class.java).findAll().size)
        }
    }
}