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
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ie.elliot.api.model.Airline
import ie.elliot.api.model.ApiRealmModule
import ie.elliot.api.model.Flight
import ie.elliot.trvl.ui.corutine.Android
import io.realm.Realm
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import io.realm.RealmConfiguration
import io.realm.RealmRecyclerViewAdapter
import kotlinx.coroutines.experimental.launch
import org.junit.After

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
    private val mockContext by lazy { InstrumentationRegistry.getTargetContext().applicationContext }
    private val mockRecyclerView by lazy { RecyclerView(mockContext) }
    private val flightResultAdapter by lazy { FlightResultsAdapter() }
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
        Realm.setDefaultConfiguration(mockRealmConfig)

        launch(Android) {
            mockRecyclerView.adapter = flightResultAdapter
            mockRecyclerView.layoutManager = LinearLayoutManager(mockContext)
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
            assertEquals(flightResultAdapter.itemCount, 0)
        }
    }

    @Test
    fun testWithData() {
        launch(Android) {
            Handler(Looper.getMainLooper()).post {
                assertEquals(flightResultAdapter.itemCount, 0)
                realmInsertTestData()

                // Sleep for a bit so the async query can update.
                Thread.sleep(100)
                assertEquals(flightResultAdapter.itemCount, 2)
            }
        }
        Thread.sleep(500)
    }

    private fun realmInsertTestData() {
        Realm.getDefaultInstance().use { realm ->
            val airline = Airline("1", "swiss_air.png", "Swiss Air")
            val flightOne = Flight("1", airline, "123", "1969-07-16 13:32:00Z", "1969-07-20 20:18:04Z", "13h 40m", 0)
            val flightTwo = Flight("2", airline, "321", "1969-07-16 13:32:00Z", "1969-07-20 20:18:04Z", "13h 40m", 0)

            realm.executeTransaction { realm ->
                realm.insertOrUpdate(flightOne)
                realm.insertOrUpdate(flightTwo)
            }
            assertEquals(realm.where(Flight::class.java).findAll().size, 2)
        }
    }
}