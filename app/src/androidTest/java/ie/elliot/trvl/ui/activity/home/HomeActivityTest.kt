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
package ie.elliot.trvl.ui.activity.home

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import ie.elliot.trvl.R
import ie.elliot.trvl.ui.activity.airport_search.AirportSearchActivity
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author Elliot Tormey
 * @since 13/06/2017
 */
class HomeActivityTest {
    private companion object {
        @get:Rule
        val activityTestRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java)
    }

    @Before
    fun setUp() {
        activityTestRule.launchActivity(null)
    }

    @Test
    fun testDepartClick() {
        Intents.init()

        onView(withId(R.id.avDepart)).perform(click())
        intended(allOf(
                hasComponent(AirportSearchActivity::class.java.name),
                hasExtras(hasEntry(equalTo("hint_res_id"), equalTo(R.string.depart_from)))))

        Intents.release()
    }

    @Test
    fun testArriveClick() {
        Intents.init()

        onView(withId(R.id.avArrive)).perform(click())
        intended(allOf(
                hasComponent(AirportSearchActivity::class.java.name),
                hasExtras(hasEntry(equalTo("hint_res_id"), equalTo(R.string.where_to)))))

        Intents.release()
    }
}